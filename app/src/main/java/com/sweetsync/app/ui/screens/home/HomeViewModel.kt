package com.sweetsync.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweetsync.app.data.model.FamilyMember
import com.sweetsync.app.data.model.HealthRecord
import com.sweetsync.app.data.model.UserProfile
import com.sweetsync.app.data.repository.AuthRepository
import com.sweetsync.app.data.repository.HealthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val currentUser: UserProfile = UserProfile(),
    val familyMembers: List<FamilyMember> = emptyList(),
    val healthRecords: List<HealthRecord> = emptyList(),
    val selectedDate: String = "2024-10-24",
    val showQuickLogSheet: Boolean = false,
    val quickLogMemberId: String? = null
)

class HomeViewModel(
    private val healthRepository: HealthRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow("2024-10-24")
    private val _showQuickLog = MutableStateFlow(false)
    private val _quickLogMemberId = MutableStateFlow<String?>(null)

    private val _quickLogState = combine(_showQuickLog, _quickLogMemberId) { show, id -> Pair(show, id) }

    val uiState: StateFlow<HomeUiState> = combine(
        authRepository.currentUser,
        healthRepository.familyMembers,
        healthRepository.healthRecords,
        _selectedDate,
        _quickLogState
    ) { user, members, records, date, quickLog ->
        HomeUiState(
            currentUser = user,
            familyMembers = members,
            healthRecords = records,
            selectedDate = date,
            showQuickLogSheet = quickLog.first,
            quickLogMemberId = quickLog.second
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeUiState()
    )

    fun onDateSelected(date: String) {
        _selectedDate.value = date
    }

    fun openQuickLog(memberId: String? = null) {
        _quickLogMemberId.value = memberId
        _showQuickLog.value = true
    }

    fun closeQuickLog() {
        _showQuickLog.value = false
        _quickLogMemberId.value = null
    }

    fun saveQuickLogRecord(record: HealthRecord) {
        viewModelScope.launch {
            healthRepository.addHealthRecord(record)
        }
    }

    fun toggleSync() {
        authRepository.toggleMockSyncStatus()
    }
}
