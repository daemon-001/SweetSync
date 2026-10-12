package com.daemon.sweetsync.ui.screens.addmember

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daemon.sweetsync.data.model.FamilyMember
import com.daemon.sweetsync.data.model.TrackType
import com.daemon.sweetsync.data.repository.HealthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AddMemberUiState(
    val fullName: String = "David Miller",
    val selectedPreset: String = "Elder / Dad",
    val relationship: String = "Parent",
    val age: String = "67",
    val gender: String = "Male",
    val doctorNotes: String = "",
    val isDoctorNotesExpanded: Boolean = false,
    val trackSugarEnabled: Boolean = true,
    val trackBpEnabled: Boolean = true,
    val trackWeightEnabled: Boolean = true,
    val morningReminderEnabled: Boolean = true,
    val postLunchReminderEnabled: Boolean = true,
    val isSavedSuccess: Boolean = false,
    val savedMemberId: String? = null
)

class AddMemberViewModel(
    private val healthRepository: HealthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddMemberUiState())
    val uiState: StateFlow<AddMemberUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(fullName = name)
    }

    fun onPresetSelected(preset: String) {
        when (preset) {
            "Elder / Dad" -> {
                _uiState.value = _uiState.value.copy(
                    selectedPreset = preset,
                    fullName = "David Miller",
                    relationship = "Parent",
                    gender = "Male",
                    age = "67",
                    trackSugarEnabled = true,
                    trackBpEnabled = true,
                    trackWeightEnabled = true
                )
            }
            "Mom" -> {
                _uiState.value = _uiState.value.copy(
                    selectedPreset = preset,
                    fullName = "Elena Miller",
                    relationship = "Parent",
                    gender = "Female",
                    age = "58",
                    trackSugarEnabled = true,
                    trackBpEnabled = true,
                    trackWeightEnabled = true
                )
            }
            "Partner" -> {
                _uiState.value = _uiState.value.copy(
                    selectedPreset = preset,
                    fullName = "Partner Miller",
                    relationship = "Spouse",
                    gender = "Female",
                    age = "34",
                    trackSugarEnabled = false,
                    trackBpEnabled = true,
                    trackWeightEnabled = true
                )
            }
            "Child" -> {
                _uiState.value = _uiState.value.copy(
                    selectedPreset = preset,
                    fullName = "Lucas Miller",
                    relationship = "Child",
                    gender = "Male",
                    age = "19",
                    trackSugarEnabled = false,
                    trackBpEnabled = false,
                    trackWeightEnabled = true
                )
            }
        }
    }

    fun onRelationshipSelected(rel: String) {
        _uiState.value = _uiState.value.copy(relationship = rel)
    }

    fun onAgeChange(age: String) {
        _uiState.value = _uiState.value.copy(age = age)
    }

    fun onGenderSelected(gender: String) {
        _uiState.value = _uiState.value.copy(gender = gender)
    }

    fun toggleDoctorNotes() {
        _uiState.value = _uiState.value.copy(isDoctorNotesExpanded = !_uiState.value.isDoctorNotesExpanded)
    }

    fun onDoctorNotesChange(notes: String) {
        _uiState.value = _uiState.value.copy(doctorNotes = notes)
    }

    fun toggleSugarTrack(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(trackSugarEnabled = enabled)
    }

    fun toggleBpTrack(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(trackBpEnabled = enabled)
    }

    fun toggleWeightTrack(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(trackWeightEnabled = enabled)
    }

    fun toggleMorningReminder(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(morningReminderEnabled = enabled)
    }

    fun togglePostLunchReminder(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(postLunchReminderEnabled = enabled)
    }

    fun saveMember(onSuccess: (String) -> Unit) {
        val s = _uiState.value
        val tracks = mutableListOf<String>()
        if (s.trackSugarEnabled) tracks.add(TrackType.BLOOD_SUGAR.code)
        if (s.trackBpEnabled) tracks.add(TrackType.BLOOD_PRESSURE.code)
        if (s.trackWeightEnabled) tracks.add(TrackType.WEIGHT.code)

        val member = FamilyMember(
            fullName = s.fullName.ifBlank { "New Family Member" },
            relationship = s.relationship,
            age = s.age.toIntOrNull() ?: 60,
            gender = s.gender,
            avatarDrawableName = if (s.gender == "Male") "avatar_robert" else "avatar_elena",
            activeTracks = tracks,
            remindersEnabled = mapOf(
                "fasting" to s.morningReminderEnabled,
                "post_lunch" to s.postLunchReminderEnabled
            ),
            lastLoggedText = "Just configured",
            deviceSyncName = "Bluetooth Monitor",
            doctorNotes = s.doctorNotes.ifBlank { null }
        )

        viewModelScope.launch {
            val result = healthRepository.addFamilyMember(member)
            result.onSuccess { added ->
                _uiState.value = _uiState.value.copy(
                    isSavedSuccess = true,
                    savedMemberId = added.id
                )
                onSuccess(added.id)
            }
        }
    }
}
