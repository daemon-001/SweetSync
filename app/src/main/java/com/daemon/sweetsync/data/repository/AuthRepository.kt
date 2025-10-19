package com.daemon.sweetsync.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class UserProfile(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val created_at: Long = 0L
) {
    // No-arg constructor for Firebase
    constructor() : this("", "", "", 0L)
}

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseClient: FirebaseClient
) {
    private val auth: FirebaseAuth = firebaseClient.auth
    private val firestore: FirebaseFirestore = firebaseClient.firestore

    /**
     * Sign up a new user with email, password, and name
     */
    suspend fun signUp(email: String, password: String, name: String): Result<Unit> {
        return try {
            // Create user with email and password
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: throw Exception("User creation failed")

            // Update user profile with display name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user.updateProfile(profileUpdates).await()

            // Create user profile in Firestore
            val userProfile = UserProfile(
                id = user.uid,
                email = email,
                name = name,
                created_at = System.currentTimeMillis()
            )

            firestore.collection(FirebaseClient.COLLECTION_USER_PROFILES)
                .document(user.uid)
                .set(userProfile)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sign in an existing user
     */
    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sign out the current user
     */
    suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get the user profile from Firestore
     */
    suspend fun getUserProfile(): Result<UserProfile?> {
        return try {
            val currentUser = getCurrentUser()
            if (currentUser != null) {
                val document = firestore.collection(FirebaseClient.COLLECTION_USER_PROFILES)
                    .document(currentUser.uid)
                    .get()
                    .await()

                val profile = document.toObject(UserProfile::class.java)
                Result.success(profile)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get the current Firebase user
     */
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    /**
     * Check if user is logged in
     */
    fun isUserLoggedIn(): Boolean = getCurrentUser() != null

    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? = getCurrentUser()?.uid

    /**
     * Send password reset email
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update user profile name
     */
    suspend fun updateUserName(newName: String): Result<Unit> {
        return try {
            val currentUser = getCurrentUser() ?: throw Exception("User not logged in")

            // Update Firebase Auth profile
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build()
            currentUser.updateProfile(profileUpdates).await()

            // Update Firestore profile
            firestore.collection(FirebaseClient.COLLECTION_USER_PROFILES)
                .document(currentUser.uid)
                .update("name", newName)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
