package com.sweetsync.app.ui.screens.bloodsugar

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

data class BloodSugarUiState(
    val familyMembers: List<FamilyMember> = emptyList(),
    val selectedMemberFilter: String = "Robert Miller", // "All Family", "Robert Miller", "Elena Miller"
    val allSugarRecords: List<HealthRecord> = emptyList(),
    val isAddEntryExpanded: Boolean = true,
    // Add form fields
    val selectedMemberId: String = "mem_robert_1",
    val fastingValue: Double = 95.0,
    val postLunchValue: Double = 132.0,
    val selectedMealTag: String = "Post-Lunch",
    val mealNote: String = "e.g. Avocado toast with poached egg",
    val timestampText: String = "Today, Oct 24 • 01:45 PM"
)

class BloodSugarViewModel(
    private val healthRepository: HealthRepository
) : ViewModel() {

    private val _selectedMemberFilter = MutableStateFlow("Robert Miller")
    private val _isAddExpanded = MutableStateFlow(true)
    private val _selectedMemberId = MutableStateFlow("mem_robert_1")
    private val _fastingVal = MutableStateFlow(95.0)
    private val _postLunchVal = MutableStateFlow(132.0)
    private val _selectedMealTag = MutableStateFlow("Post-Lunch")
    private val _mealNote = MutableStateFlow("")

    val uiState: StateFlow<BloodSugarUiState> = combine(
        healthRepository.familyMembers,
        healthRepository.healthRecords,
        _selectedMemberFilter,
        _isAddExpanded,
        _selectedMemberId
    ) { members, records, filter, isExp, memId ->
        val sugarRecs = records.filter { it.trackType == TrackType.BLOOD_SUGAR.code }
        val filtered = if (filter == "All Family") {
            sugarRecs
        } else {
            sugarRecs.filter { it.memberName.contains(filter) }
        }

        BloodSugarUiState(
            familyMembers = members,
            selectedMemberFilter = filter,
            allSugarRecords = filtered,
            isAddEntryExpanded = isExp,
            selectedMemberId = memId,
            fastingValue = _fastingVal.value,
            postLunchValue = _postLunchVal.value,
            selectedMealTag = _selectedMealTag.value,
            mealNote = _mealNote.value
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        BloodSugarUiState()
    )

    fun onFilterChange(filter: String) {
        _selectedMemberFilter.value = filter
    }

    fun toggleAddSection() {
        _isAddExpanded.value = !_isAddExpanded.value
    }

    fun onMemberSelected(memberId: String) {
        _selectedMemberId.value = memberId
    }

    fun onFastingChange(value: Double) {
        _fastingVal.value = value
    }

    fun onPostLunchChange(value: Double) {
        _postLunchVal.value = value
    }

    fun onMealTagSelected(tag: String) {
        _selectedMealTag.value = tag
    }

    fun onMealNoteChange(note: String) {
        _mealNote.value = note
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
            trackType = TrackType.BLOOD_SUGAR.code,
            timestamp = now.time,
            dateString = sdfDate.format(now),
            timeString = sdfTime.format(now),
            sugarFasting = _fastingVal.value,
            sugarPostLunch = _postLunchVal.value,
            mealTag = _selectedMealTag.value,
            mealNote = _mealNote.value.ifBlank { null },
            statusText = if (_fastingVal.value < 100 && _postLunchVal.value < 140) "Target Optimal" else "Normal",
            statusLevel = StatusLevel.OPTIMAL
        )

        viewModelScope.launch {
            healthRepository.addHealthRecord(record)
            _mealNote.value = ""
        }
    }
}
