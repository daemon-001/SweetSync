package com.daemon.sweetsync.data.repository

import com.daemon.sweetsync.data.model.BloodSugarReading
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BloodSugarRepository @Inject constructor(
    private val firebaseClient: FirebaseClient,
    private val authRepository: AuthRepository
) {
    private val firestore: FirebaseFirestore = firebaseClient.firestore

    /**
     * Insert a new blood sugar reading
     */
    suspend fun insertReading(reading: BloodSugarReading): Result<Unit> {
        return try {
            val userId = authRepository.getCurrentUserId() 
                ?: throw Exception("User not logged in")
            
            val readingWithUser = reading.copy(user_id = userId)

            firestore.collection(FirebaseClient.COLLECTION_BLOOD_SUGAR_READINGS)
                .add(readingWithUser)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all readings for the current user
     */
    suspend fun getReadings(): Result<List<BloodSugarReading>> {
        return try {
            val userId = authRepository.getCurrentUserId() 
                ?: throw Exception("User not logged in")

            val querySnapshot = firestore.collection(FirebaseClient.COLLECTION_BLOOD_SUGAR_READINGS)
                .whereEqualTo("user_id", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val readings = querySnapshot.documents.mapNotNull { document ->
                document.toObject(BloodSugarReading::class.java)?.copy(
                    id = document.id
                )
            }

            Result.success(readings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get readings as a Flow for real-time updates
     */
    fun getReadingsFlow(): Flow<List<BloodSugarReading>> = callbackFlow {
        val userId = authRepository.getCurrentUserId()
        
        if (userId == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listenerRegistration = firestore.collection(FirebaseClient.COLLECTION_BLOOD_SUGAR_READINGS)
            .whereEqualTo("user_id", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val readings = snapshot.documents.mapNotNull { document ->
                        document.toObject(BloodSugarReading::class.java)?.copy(
                            id = document.id
                        )
                    }
                    trySend(readings)
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

    /**
     * Update an existing reading
     */
    suspend fun updateReading(reading: BloodSugarReading): Result<Unit> {
        return try {
            val userId = authRepository.getCurrentUserId() 
                ?: throw Exception("User not logged in")
            
            val readingId = reading.id ?: throw Exception("Reading ID is required for update")

            // Verify ownership
            val document = firestore.collection(FirebaseClient.COLLECTION_BLOOD_SUGAR_READINGS)
                .document(readingId)
                .get()
                .await()

            val existingReading = document.toObject(BloodSugarReading::class.java)
            if (existingReading?.user_id != userId) {
                throw Exception("Unauthorized: Cannot update another user's reading")
            }

            firestore.collection(FirebaseClient.COLLECTION_BLOOD_SUGAR_READINGS)
                .document(readingId)
                .set(reading)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete a reading
     */
    suspend fun deleteReading(readingId: String): Result<Unit> {
        return try {
            val userId = authRepository.getCurrentUserId() 
                ?: throw Exception("User not logged in")

            // Verify ownership
            val document = firestore.collection(FirebaseClient.COLLECTION_BLOOD_SUGAR_READINGS)
                .document(readingId)
                .get()
                .await()

            val existingReading = document.toObject(BloodSugarReading::class.java)
            if (existingReading?.user_id != userId) {
                throw Exception("Unauthorized: Cannot delete another user's reading")
            }

            firestore.collection(FirebaseClient.COLLECTION_BLOOD_SUGAR_READINGS)
                .document(readingId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get readings within a date range
     */
    suspend fun getReadingsByDateRange(startTimestamp: Long, endTimestamp: Long): Result<List<BloodSugarReading>> {
        return try {
            val userId = authRepository.getCurrentUserId() 
                ?: throw Exception("User not logged in")

            val querySnapshot = firestore.collection(FirebaseClient.COLLECTION_BLOOD_SUGAR_READINGS)
                .whereEqualTo("user_id", userId)
                .whereGreaterThanOrEqualTo("timestamp", startTimestamp)
                .whereLessThanOrEqualTo("timestamp", endTimestamp)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val readings = querySnapshot.documents.mapNotNull { document ->
                document.toObject(BloodSugarReading::class.java)?.copy(
                    id = document.id
                )
            }

            Result.success(readings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get average glucose level for the current user
     */
    suspend fun getAverageGlucoseLevel(): Result<Double> {
        return try {
            val readings = getReadings()
            if (readings.isSuccess) {
                val readingsList = readings.getOrNull() ?: emptyList()
                val average = if (readingsList.isNotEmpty()) {
                    readingsList.map { it.glucose_level }.average()
                } else {
                    0.0
                }
                Result.success(average)
            } else {
                Result.failure(readings.exceptionOrNull() ?: Exception("Failed to get readings"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get total reading count for the current user
     */
    suspend fun getReadingCount(): Result<Int> {
        return try {
            val readings = getReadings()
            if (readings.isSuccess) {
                Result.success(readings.getOrNull()?.size ?: 0)
            } else {
                Result.failure(readings.exceptionOrNull() ?: Exception("Failed to get readings"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
