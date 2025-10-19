package com.daemon.sweetsync.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daemon.sweetsync.data.repository.AuthRepository
import com.daemon.sweetsync.data.repository.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext

data class AuthUiState(
    val isLoading: Boolean = true,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null,
    val userProfile: UserProfile? = null,
    val verificationEmailSent: Boolean = false,
    val isSignUpSuccess: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Local persistent storage for login state
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("auth_persistence", Context.MODE_PRIVATE)
    }

    private companion object {
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val KEY_USER_EMAIL = "user_email"
        const val KEY_USER_NAME = "user_name"
        const val KEY_USER_ID = "user_id"
    }

    fun signUp(email: String, password: String, name: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            authRepository.signUp(email, password, name)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        verificationEmailSent = true,
                        isSignUpSuccess = true,
                        errorMessage = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Sign up failed",
                        verificationEmailSent = false,
                        isSignUpSuccess = false
                    )
                }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            authRepository.signIn(email, password)
                .onSuccess {
                    // Save login state locally - this makes it permanent
                    saveLoginState(true)

                    // Set authenticated state immediately
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        errorMessage = null
                    )

                    // Try to get user profile in background, but don't wait for it
                    getUserProfileInBackground(email)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Sign in failed"
                    )
                }
        }
    }

    private fun getUserProfileInBackground(email: String) {
        viewModelScope.launch {
            authRepository.getUserProfile()
                .onSuccess { profile ->
                    // Only save if profile is not null
                    profile?.let {
                        saveUserInfo(it)
                        _uiState.value = _uiState.value.copy(userProfile = it)
                    } ?: run {
                        // Profile is null, create basic profile
                        val basicProfile = UserProfile(
                            id = "",
                            email = email,
                            name = getSavedUserName() ?: email.substringBefore("@"),
                            created_at = System.currentTimeMillis()
                        )
                        saveUserInfo(basicProfile)
                        _uiState.value = _uiState.value.copy(userProfile = basicProfile)
                    }
                }
                .onFailure {
                    // Even if profile fetch fails, create a basic profile
                    val basicProfile = UserProfile(
                        id = "",
                        email = email,
                        name = getSavedUserName() ?: email.substringBefore("@"),
                        created_at = System.currentTimeMillis()
                    )
                    saveUserInfo(basicProfile)
                    _uiState.value = _uiState.value.copy(userProfile = basicProfile)
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            // Clear local login state first - this is crucial
            saveLoginState(false)
            clearUserInfo()

            // Set unauthenticated state immediately
            _uiState.value = AuthUiState(
                isLoading = false,
                isAuthenticated = false,
                userProfile = null,
                errorMessage = null,
                verificationEmailSent = false,
                isSignUpSuccess = false
            )

            // Try to sign out from repository in background, but don't wait for it
            try {
                authRepository.signOut()
            } catch (e: Exception) {
                // Ignore remote signout failures - local state is already cleared
            }
        }
    }

    fun checkAuthStatus() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Check local login state first
            val isLocallyLoggedIn = getSavedLoginState()

            if (isLocallyLoggedIn) {
                // User is locally logged in, restore their session
                val savedProfile = getSavedUserProfile()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = true,
                    userProfile = savedProfile
                )

                // Optionally try to refresh profile in background, but don't fail
                refreshUserProfileSilently()
            } else {
                // User is not logged in
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = false,
                    userProfile = null
                )
            }
        }
    }

    private fun refreshUserProfileSilently() {
        viewModelScope.launch {
            authRepository.getUserProfile()
                .onSuccess { profile ->
                    profile?.let {
                        saveUserInfo(it)
                        // Only update if still authenticated
                        if (_uiState.value.isAuthenticated) {
                            _uiState.value = _uiState.value.copy(userProfile = it)
                        }
                    }
                }
                .onFailure {
                    // Ignore failures during silent refresh
                    // Keep existing profile and login state
                }
        }
    }

    fun refreshUserProfile() {
        viewModelScope.launch {
            if (_uiState.value.isAuthenticated) {
                authRepository.getUserProfile()
                    .onSuccess { profile ->
                        profile?.let {
                            saveUserInfo(it)
                            _uiState.value = _uiState.value.copy(userProfile = it)
                        }
                    }
                    .onFailure {
                        // Don't sign out, just show error
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "Failed to refresh profile"
                        )
                    }
            }
        }
    }

    // Local storage methods
    private fun saveLoginState(isLoggedIn: Boolean) {
        try {
            sharedPreferences.edit {
                putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            }
        } catch (e: Exception) {
            // Handle silently
        }
    }

    private fun getSavedLoginState(): Boolean {
        return try {
            sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        } catch (e: Exception) {
            false
        }
    }

    private fun saveUserInfo(profile: UserProfile) {
        try {
            sharedPreferences.edit {
                putString(KEY_USER_EMAIL, profile.email)
                putString(KEY_USER_NAME, profile.name)
                putString(KEY_USER_ID, profile.id)
            }
        } catch (e: Exception) {
            // Handle silently
        }
    }

    private fun getSavedUserProfile(): UserProfile? {
        return try {
            val email = sharedPreferences.getString(KEY_USER_EMAIL, null)
            val name = sharedPreferences.getString(KEY_USER_NAME, null)
            val id = sharedPreferences.getString(KEY_USER_ID, null)

            if (email != null) {
                UserProfile(
                    id = id ?: "",
                    email = email,
                    name = name ?: email.substringBefore("@"),
                    created_at = 0L // Default timestamp for cached profiles
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private fun getSavedUserName(): String? {
        return try {
            sharedPreferences.getString(KEY_USER_NAME, null)
        } catch (e: Exception) {
            null
        }
    }

    private fun clearUserInfo() {
        try {
            sharedPreferences.edit {
                remove(KEY_USER_EMAIL)
                remove(KEY_USER_NAME)
                remove(KEY_USER_ID)
            }
        } catch (e: Exception) {
            // Handle silently
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearVerificationMessage() {
        _uiState.value = _uiState.value.copy(
            verificationEmailSent = false,
            isSignUpSuccess = false
        )
    }

    fun resetToSignIn() {
        _uiState.value = _uiState.value.copy(
            verificationEmailSent = false,
            isSignUpSuccess = false,
            errorMessage = null
        )
    }
}