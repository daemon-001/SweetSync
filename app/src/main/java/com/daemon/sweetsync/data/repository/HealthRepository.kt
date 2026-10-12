package com.daemon.sweetsync.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.daemon.sweetsync.data.model.FamilyMember
import com.daemon.sweetsync.data.model.HealthRecord
import com.daemon.sweetsync.data.model.TrackType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class HealthRepository(
    private val authRepository: AuthRepository
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val firestore: FirebaseFirestore? = try {
        FirebaseFirestore.getInstance()
    } catch (e: Exception) {
        null
    }

    private val _familyMembers = MutableStateFlow<List<FamilyMember>>(MockDataProvider.defaultMembers)
    val familyMembers: StateFlow<List<FamilyMember>> = _familyMembers.asStateFlow()

    private val _healthRecords = MutableStateFlow<List<HealthRecord>>(MockDataProvider.defaultRecords)
    val healthRecords: StateFlow<List<HealthRecord>> = _healthRecords.asStateFlow()

    private var membersListener: ListenerRegistration? = null
    private var recordsListener: ListenerRegistration? = null

    init {
        scope.launch {
            authRepository.currentUser.collect { user ->
                setupFirestoreListeners(user.userId)
            }
        }
    }

    private fun setupFirestoreListeners(userId: String) {
        if (firestore == null || userId.isBlank()) return

        membersListener?.remove()
        recordsListener?.remove()

        try {
            val userDoc = firestore.collection("users").document(userId)
            membersListener = userDoc.collection("family_members").addSnapshotListener { snapshot, _ ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(FamilyMember::class.java)?.copy(id = doc.id)
                    }
                    if (list.isNotEmpty()) {
                        _familyMembers.value = list
                    }
                }
            }

            recordsListener = userDoc.collection("health_records").addSnapshotListener { snapshot, _ ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(HealthRecord::class.java)?.copy(id = doc.id)
                    }
                    if (list.isNotEmpty()) {
                        _healthRecords.value = list
                    }
                }
            }
        } catch (_: Exception) {
            // Graceful fallback to local cache
        }
    }

    suspend fun addFamilyMember(member: FamilyMember): Result<FamilyMember> {
        val id = if (member.id.isBlank()) "mem_${UUID.randomUUID().toString().take(8)}" else member.id
        val memberWithId = member.copy(id = id)
        
        // Update local state immediately
        val current = _familyMembers.value.toMutableList()
        current.add(0, memberWithId)
        _familyMembers.value = current

        // Sync to Firestore
        try {
            val userId = authRepository.currentUser.value.userId
            firestore?.collection("users")?.document(userId)
                ?.collection("family_members")?.document(id)
                ?.set(memberWithId)?.await()
        } catch (_: Exception) {
            // Keep local
        }

        return Result.success(memberWithId)
    }

    suspend fun updateFamilyMember(member: FamilyMember): Result<Unit> {
        val current = _familyMembers.value.toMutableList()
        val index = current.indexOfFirst { it.id == member.id }
        if (index != -1) {
            current[index] = member
            _familyMembers.value = current
        }

        try {
            val userId = authRepository.currentUser.value.userId
            firestore?.collection("users")?.document(userId)
                ?.collection("family_members")?.document(member.id)
                ?.set(member)?.await()
        } catch (_: Exception) {
            // Keep local
        }
        return Result.success(Unit)
    }

    suspend fun deleteFamilyMember(memberId: String): Result<Unit> {
        val current = _familyMembers.value.toMutableList()
        current.removeAll { it.id == memberId }
        _familyMembers.value = current

        try {
            val userId = authRepository.currentUser.value.userId
            firestore?.collection("users")?.document(userId)
                ?.collection("family_members")?.document(memberId)
                ?.delete()?.await()
        } catch (_: Exception) {
            // Keep local
        }
        return Result.success(Unit)
    }

    suspend fun addHealthRecord(record: HealthRecord): Result<HealthRecord> {
        val id = if (record.id.isBlank()) "rec_${UUID.randomUUID().toString().take(8)}" else record.id
        val finalRecord = record.copy(id = id)

        // Update local state immediately
        val current = _healthRecords.value.toMutableList()
        current.add(0, finalRecord)
        _healthRecords.value = current

        // Sync to Firestore
        try {
            val userId = authRepository.currentUser.value.userId
            firestore?.collection("users")?.document(userId)
                ?.collection("health_records")?.document(id)
                ?.set(finalRecord)?.await()
        } catch (_: Exception) {
            // Keep local
        }

        return Result.success(finalRecord)
    }

    fun getMember(memberId: String): FamilyMember? {
        return _familyMembers.value.find { it.id == memberId }
    }

    fun getRecordsForMember(memberId: String): List<HealthRecord> {
        return _healthRecords.value.filter { it.memberId == memberId }
    }

    fun getRecordsForTrack(trackType: String, memberId: String? = null): List<HealthRecord> {
        return _healthRecords.value.filter { record ->
            record.trackType == trackType && (memberId == null || record.memberId == memberId)
        }
    }

    fun getRecordsForDate(dateString: String, memberId: String? = null): List<HealthRecord> {
        return _healthRecords.value.filter { record ->
            record.dateString == dateString && (memberId == null || record.memberId == memberId)
        }
    }

    fun getMonthlyRecordMap(yearMonth: String, memberId: String? = null): Map<String, Set<String>> {
        // yearMonth format: "2024-10"
        val map = mutableMapOf<String, MutableSet<String>>()
        val records = _healthRecords.value.filter {
            it.dateString.startsWith(yearMonth) && (memberId == null || it.memberId == memberId)
        }
        for (rec in records) {
            if (rec.trackType.isNotBlank()) {
                val set = map.getOrPut(rec.dateString) { mutableSetOf() }
                set.add(rec.trackType)
            }
        }
        return map
    }
}
