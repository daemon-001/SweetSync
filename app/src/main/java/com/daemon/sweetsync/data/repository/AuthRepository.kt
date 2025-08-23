package com.daemon.sweetsync.data.repository

import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class UserProfile(
    val id: String,
    val email: String,
    val name: String,
    val created_at: String? = null
)

@Singleton
class AuthRepository @Inject constructor(
    private val supabaseClient: SupabaseClient // This should be your existing SupabaseClient wrapper class
) {
    // Store the name temporarily during signup process
    private var pendingUserName: String? = null
    private var pendingUserEmail: String? = null

    suspend fun signUp(email: String, password: String, name: String): Result<Unit> {
        return try {
            println("DEBUG: Starting signup with name: '$name'") // Debug log

            // Store name temporarily for use during signin
            pendingUserName = name
            pendingUserEmail = email

            // Sign up the user with Supabase Auth
            supabaseClient.client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                // Store the name in user metadata using JsonObject
                data = buildJsonObject {
                    put("full_name", name)
                    put("name", name) // Add this as backup
                }
            }

            println("DEBUG: Signup completed, name stored temporarily for signin")
            Result.success(Unit)

        } catch (e: Exception) {
            println("DEBUG: Signup failed with exception: ${e.message}")
            e.printStackTrace()

            // Clear temporary storage on failure
            pendingUserName = null
            pendingUserEmail = null

            // Check if this might be a successful signup with email verification required
            val errorMessage = e.message?.lowercase() ?: ""
            if (errorMessage.contains("email") ||
                errorMessage.contains("confirmation") ||
                errorMessage.contains("verify") ||
                errorMessage.contains("check your email")) {
                // Re-store the name since it was a successful signup
                pendingUserName = name
                pendingUserEmail = email
                Result.success(Unit)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            supabaseClient.client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            // After successful sign-in, check if profile exists, if not create it
            val currentUser = getCurrentUser()
            if (currentUser != null) {
                // Check if profile already exists
                val existingProfiles = supabaseClient.client.from("user_profiles")
                    .select(Columns.ALL) {
                        filter {
                            eq("id", currentUser.id)
                        }
                    }
                    .decodeList<UserProfile>()

                if (existingProfiles.isEmpty()) {
                    // Profile doesn't exist, create it
                    val userName = determineUserName(currentUser, email)

                    println("DEBUG: Creating profile with name: '$userName'")

                    try {
                        supabaseClient.client.from("user_profiles").insert(
                            mapOf(
                                "id" to currentUser.id,
                                "email" to (currentUser.email ?: email),
                                "name" to userName
                            )
                        )
                        println("DEBUG: Profile created successfully with name: '$userName'")

                        // Clear temporary storage after successful profile creation
                        clearPendingUserData()

                    } catch (profileException: Exception) {
                        println("DEBUG: Profile creation failed: ${profileException.message}")
                        profileException.printStackTrace()
                    }
                } else {
                    // Profile exists, clear pending data
                    clearPendingUserData()
                    println("DEBUG: Profile already exists, no need to create")
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun determineUserName(currentUser: io.github.jan.supabase.gotrue.user.UserInfo, email: String): String {
        // Priority 1: Use pending name from recent signup
        if (pendingUserName != null && (pendingUserEmail == email || pendingUserEmail == currentUser.email)) {
            println("DEBUG: Using pending name from signup: '$pendingUserName'")
            return pendingUserName!!
        }

        // Priority 2: Try to extract from metadata
        val extractedName = extractNameFromMetadata(currentUser)
        if (extractedName.isNotBlank()) {
            println("DEBUG: Using name from metadata: '$extractedName'")
            return extractedName
        }

        // Priority 3: Use email prefix as fallback
        val fallbackName = email.substringBefore("@").takeIf { it.isNotBlank() } ?: "User"
        println("DEBUG: Using fallback name: '$fallbackName'")
        return fallbackName
    }

    private fun extractNameFromMetadata(currentUser: io.github.jan.supabase.gotrue.user.UserInfo): String {
        return try {
            val metadata = currentUser.userMetadata
            println("DEBUG: Available metadata: $metadata")

            // Try different possible keys
            val possibleKeys = listOf("full_name", "name", "display_name", "fullName")

            for (key in possibleKeys) {
                metadata?.get(key)?.let { nameValue ->
                    val cleanName = nameValue.toString()
                        .trim()
                        .replace("\"", "")
                        .replace("'", "")

                    if (cleanName.isNotBlank() && cleanName != "null") {
                        println("DEBUG: Found name '$cleanName' using key '$key'")
                        return cleanName
                    }
                }
            }

            ""
        } catch (e: Exception) {
            println("DEBUG: Error extracting name from metadata: ${e.message}")
            ""
        }
    }

    private fun clearPendingUserData() {
        pendingUserName = null
        pendingUserEmail = null
    }

    suspend fun signOut(): Result<Unit> {
        return try {
            // Clear any pending data
            clearPendingUserData()

            supabaseClient.client.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(): Result<UserProfile?> {
        return try {
            val currentUser = getCurrentUser()
            if (currentUser != null) {
                val profiles = supabaseClient.client.from("user_profiles")
                    .select(Columns.ALL) {
                        filter {
                            eq("id", currentUser.id)
                        }
                    }
                    .decodeList<UserProfile>()

                Result.success(profiles.firstOrNull())
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser() = supabaseClient.client.auth.currentUserOrNull()

    fun isUserLoggedIn() = getCurrentUser() != null
}