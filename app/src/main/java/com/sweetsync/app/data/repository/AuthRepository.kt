package com.sweetsync.app.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.sweetsync.app.data.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthRepository(private val context: Context) {

    private val auth: FirebaseAuth? = try {
        FirebaseAuth.getInstance()
    } catch (e: Exception) {
        null
    }

    private val _currentUser = MutableStateFlow(
        UserProfile(
            userId = auth?.currentUser?.uid ?: "user_sarah_101",
            displayName = auth?.currentUser?.displayName ?: "Sarah Miller",
            email = auth?.currentUser?.email ?: "sarah.miller@sweetsync.health",
            isGuest = auth?.currentUser?.isAnonymous ?: false
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    init {
        auth?.addAuthStateListener { fbAuth ->
            val user = fbAuth.currentUser
            if (user != null) {
                _currentUser.value = UserProfile(
                    userId = user.uid,
                    displayName = user.displayName ?: "Sarah Miller",
                    email = user.email ?: "sarah.miller@sweetsync.health",
                    isGuest = user.isAnonymous,
                    photoUrl = user.photoUrl?.toString()
                )
            }
        }
    }

    suspend fun signInAnonymously(): Result<UserProfile> {
        return try {
            if (auth != null) {
                val result = auth.signInAnonymously().await()
                val user = result.user
                val profile = UserProfile(
                    userId = user?.uid ?: "guest_${System.currentTimeMillis()}",
                    displayName = "Guest User",
                    email = "",
                    isGuest = true
                )
                _currentUser.value = profile
                Result.success(profile)
            } else {
                val profile = UserProfile(
                    userId = "guest_${System.currentTimeMillis()}",
                    displayName = "Guest User",
                    email = "",
                    isGuest = true
                )
                _currentUser.value = profile
                Result.success(profile)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun linkGoogleAccount(idToken: String): Result<UserProfile> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val currentFbUser = auth?.currentUser
            if (currentFbUser != null) {
                val result = if (currentFbUser.isAnonymous) {
                    currentFbUser.linkWithCredential(credential).await()
                } else {
                    auth.signInWithCredential(credential).await()
                }
                val user = result.user
                val profile = UserProfile(
                    userId = user?.uid ?: currentFbUser.uid,
                    displayName = user?.displayName ?: "Sarah Miller",
                    email = user?.email ?: "sarah.miller@sweetsync.health",
                    isGuest = false,
                    photoUrl = user?.photoUrl?.toString()
                )
                _currentUser.value = profile
                Result.success(profile)
            } else {
                val profile = UserProfile(
                    userId = "user_sarah_101",
                    displayName = "Sarah Miller",
                    email = "sarah.miller@sweetsync.health",
                    isGuest = false
                )
                _currentUser.value = profile
                Result.success(profile)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun toggleMockSyncStatus() {
        val current = _currentUser.value
        _currentUser.value = current.copy(isGuest = !current.isGuest)
    }

    fun signOut() {
        auth?.signOut()
        _currentUser.value = UserProfile(
            userId = "guest_${System.currentTimeMillis()}",
            displayName = "Guest User",
            email = "",
            isGuest = true
        )
    }
}
