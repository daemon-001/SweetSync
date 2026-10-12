package com.sweetsync.app.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweetsync.app.data.model.UserProfile
import com.sweetsync.app.data.repository.AuthRepository
import com.sweetsync.app.data.repository.HealthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AccountUiState(
    val userProfile: UserProfile = UserProfile(),
    val membersCount: Int = 4,
    val recordsCount: Int = 12,
    val isSyncing: Boolean = false,
    val syncSuccessMessage: String? = null
)

class AccountViewModel(
    private val authRepository: AuthRepository,
    private val healthRepository: HealthRepository
) : ViewModel() {

    val uiState: StateFlow<AccountUiState> = authRepository.currentUser.map { user ->
        val members = healthRepository.familyMembers.value.size
        val records = healthRepository.healthRecords.value.size
        AccountUiState(
            userProfile = user,
            membersCount = members,
            recordsCount = records
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AccountUiState()
    )

    fun toggleGuestGoogleLink() {
        authRepository.toggleMockSyncStatus()
    }

    fun signOut() {
        authRepository.signOut()
    }
}
