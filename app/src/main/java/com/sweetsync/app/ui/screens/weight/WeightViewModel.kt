package com.sweetsync.app.ui.screens.weight

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

data class WeightUiState(
    val familyMembers: List<FamilyMember> = emptyList(),
    val selectedMemberFilter: String = "All Family",
    val allWeightRecords: List<HealthRecord> = emptyList(),
    val unit: String = "kg",
    val fastingWeight: Double = 64.2,
    val postLunchWeight: Double = 64.9,
    val currentReading: Double = 64.2,
    val targetGoal: Double = 62.0,
    val startingWeight: Double = 66.8,
    val selectedContextFactors: List<String> = listOf("Post morning walk", "Well hydrated")
)

class WeightViewModel(
    private val healthRepository: HealthRepository
) : ViewModel() {

    private val _selectedMemberFilter = MutableStateFlow("All Family")
    private val _unit = MutableStateFlow("kg")
    private val _fastingWeight = MutableStateFlow(64.2)
    private val _postLunchWeight = MutableStateFlow(64.9)
    private val _contextFactors = MutableStateFlow(listOf("Post morning walk", "Well hydrated"))

    private data class FormState(
        val unit: String,
        val fastingWeight: Double,
        val postLunchWeight: Double,
        val contextFactors: List<String>
    )

    private val _formState = combine(
        _unit,
        _fastingWeight,
        _postLunchWeight,
        _contextFactors
    ) { u, f, p, factors ->
        FormState(u, f, p, factors)
    }

    val uiState: StateFlow<WeightUiState> = combine(
        healthRepository.familyMembers,
        healthRepository.healthRecords,
        _selectedMemberFilter,
        _formState
    ) { members, records, filter, form ->
        val wtRecs = records.filter { it.trackType == TrackType.WEIGHT.code }
        val filtered = if (filter == "All Family") {
            wtRecs
        } else {
            wtRecs.filter { it.memberName.contains(filter) }
        }

        WeightUiState(
            familyMembers = members,
            selectedMemberFilter = filter,
            allWeightRecords = filtered,
            unit = form.unit,
            fastingWeight = form.fastingWeight,
            postLunchWeight = form.postLunchWeight,
            selectedContextFactors = form.contextFactors
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        WeightUiState()
    )

    fun onFilterChange(filter: String) {
        _selectedMemberFilter.value = filter
    }

    fun onUnitChange(u: String) {
        _unit.value = u
    }

    fun onFastingWeightChange(w: Double) {
        _fastingWeight.value = w
    }

    fun onPostLunchWeightChange(w: Double) {
        _postLunchWeight.value = w
    }

    fun toggleFactor(factor: String) {
        val cur = _contextFactors.value.toMutableList()
        if (cur.contains(factor)) cur.remove(factor) else cur.add(factor)
        _contextFactors.value = cur
    }

    fun saveRecord() {
        val members = uiState.value.familyMembers
        val sarah = members.find { it.fullName.contains("Sarah") } ?: members.firstOrNull()
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val sdfTime = SimpleDateFormat("hh:mm a", Locale.US)
        val now = Date()

        val record = HealthRecord(
            memberId = sarah?.id ?: "mem_sarah_4",
            memberName = sarah?.fullName ?: "Sarah Miller",
            trackType = TrackType.WEIGHT.code,
            timestamp = now.time,
            dateString = sdfDate.format(now),
            timeString = sdfTime.format(now),
            weightFasting = _fastingWeight.value,
            weightPostLunch = _postLunchWeight.value,
            unit = _unit.value,
            contextTags = _contextFactors.value,
            weightDeltaVsYesterday = "-0.2 kg vs yesterday",
            statusText = "Primary",
            statusLevel = StatusLevel.OPTIMAL
        )

        viewModelScope.launch {
            healthRepository.addHealthRecord(record)
        }
    }
}
