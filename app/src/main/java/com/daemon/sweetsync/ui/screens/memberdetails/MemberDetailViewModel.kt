package com.daemon.sweetsync.ui.screens.memberdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daemon.sweetsync.data.model.FamilyMember
import com.daemon.sweetsync.data.model.HealthRecord
import com.daemon.sweetsync.data.model.TrackType
import com.daemon.sweetsync.data.repository.HealthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MemberDetailUiState(
    val member: FamilyMember? = null,
    val selectedDate: String = "2024-10-24",
    val dayRecords: List<HealthRecord> = emptyList(),
    val monthlyTrackMap: Map<String, Set<String>> = emptyMap(),
    val showQuickLogSheet: Boolean = false
)

class MemberDetailViewModel(
    private val memberId: String,
    private val healthRepository: HealthRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow("2024-10-24")
    private val _showQuickLog = MutableStateFlow(false)

    val uiState: StateFlow<MemberDetailUiState> = combine(
        healthRepository.familyMembers,
        healthRepository.healthRecords,
        _selectedDate,
        _showQuickLog
    ) { members, records, date, showLog ->
        val currentMember = members.find { it.id == memberId } ?: members.firstOrNull()
        val dayRecs = records.filter { it.memberId == (currentMember?.id ?: "") && it.dateString == date }
        val monthMap = healthRepository.getMonthlyRecordMap("2024-10", currentMember?.id)

        MemberDetailUiState(
            member = currentMember,
            selectedDate = date,
            dayRecords = dayRecs,
            monthlyTrackMap = monthMap,
            showQuickLogSheet = showLog
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MemberDetailUiState()
    )

    fun onDateSelected(date: String) {
        _selectedDate.value = date
    }

    fun openQuickLog() {
        _showQuickLog.value = true
    }

    fun closeQuickLog() {
        _showQuickLog.value = false
    }

    fun assignWeightTrack() {
        val currentMember = uiState.value.member ?: return
        if (!currentMember.activeTracks.contains(TrackType.WEIGHT.code)) {
            val updated = currentMember.copy(
                activeTracks = currentMember.activeTracks + TrackType.WEIGHT.code
            )
            viewModelScope.launch {
                healthRepository.updateFamilyMember(updated)
            }
        }
    }

    fun saveQuickLogRecord(record: HealthRecord) {
        viewModelScope.launch {
            healthRepository.addHealthRecord(record)
        }
    }
}
