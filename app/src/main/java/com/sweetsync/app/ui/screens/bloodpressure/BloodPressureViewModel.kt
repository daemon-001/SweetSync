package com.sweetsync.app.ui.screens.bloodpressure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweetsync.app.data.model.FamilyMember
import com.sweetsync.app.data.model.HealthRecord
import com.sweetsync.app.data.model.StatusLevel
import com.sweetsync.app.data.model.TrackType
import com.sweetsync.app.data.repository.HealthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class BloodPressureUiState(
    val familyMembers: List<FamilyMember> = emptyList(),
    val selectedMemberFilter: String = "Robert Miller (Active)",
    val allBpRecords: List<HealthRecord> = emptyList(),
    // Form fields
    val selectedMemberId: String = "mem_robert_1",
    val systolic: Int = 124,
    val diastolic: Int = 82,
    val heartRate: Int = 72,
    val postureContext: String = "Left Arm • Sitting • Resting 5 min",
    val dateTimeText: String = "Oct 24 • 08:15 AM"
)

class BloodPressureViewModel(
    private val healthRepository: HealthRepository
) : ViewModel() {

    private val _selectedMemberFilter = MutableStateFlow("Robert Miller (Active)")
    private val _selectedMemberId = MutableStateFlow("mem_robert_1")
    private val _systolic = MutableStateFlow(124)
    private val _diastolic = MutableStateFlow(82)
    private val _heartRate = MutableStateFlow(72)
    private val _posture = MutableStateFlow("Left Arm • Sitting • Resting 5 min")

    val uiState: StateFlow<BloodPressureUiState> = combine(
        healthRepository.familyMembers,
        healthRepository.healthRecords,
        _selectedMemberFilter,
        _selectedMemberId
    ) { members, records, filter, memId ->
        val bpRecs = records.filter { it.trackType == TrackType.BLOOD_PRESSURE.code }
        val filtered = if (filter.contains("All")) {
            bpRecs
        } else {
            val name = filter.replace(" (Active)", "")
            bpRecs.filter { it.memberName.contains(name) }
        }

        BloodPressureUiState(
            familyMembers = members,
            selectedMemberFilter = filter,
            allBpRecords = filtered,
            selectedMemberId = memId,
            systolic = _systolic.value,
            diastolic = _diastolic.value,
            heartRate = _heartRate.value,
            postureContext = _posture.value
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        BloodPressureUiState()
    )

    fun onFilterChange(filter: String) {
        _selectedMemberFilter.value = filter
    }

    fun onSystolicChange(sys: Int) {
        _systolic.value = sys
    }

    fun onDiastolicChange(dia: Int) {
        _diastolic.value = dia
    }

    fun onHeartRateChange(hr: Int) {
        _heartRate.value = hr
    }

    fun onPostureChange(p: String) {
        _posture.value = p
    }

    fun saveRecord() {
        val members = uiState.value.familyMembers
        val currentMember = members.find { it.id == _selectedMemberId.value } ?: members.firstOrNull()
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val sdfTime = SimpleDateFormat("hh:mm a", Locale.US)
        val now = Date()

        val record = HealthRecord(
            memberId = _selectedMemberId.value,
            memberName = currentMember?.fullName ?: "Robert Miller",
            trackType = TrackType.BLOOD_PRESSURE.code,
            timestamp = now.time,
            dateString = sdfDate.format(now),
            timeString = sdfTime.format(now),
            systolic = _systolic.value,
            diastolic = _diastolic.value,
            heartRate = _heartRate.value,
            postureContext = _posture.value,
            bpTag = "Morning Baseline • Left Arm",
            statusText = if (_systolic.value <= 120 && _diastolic.value <= 80) "Optimal" else "Normal",
            statusLevel = StatusLevel.OPTIMAL
        )

        viewModelScope.launch {
            healthRepository.addHealthRecord(record)
        }
    }
}
