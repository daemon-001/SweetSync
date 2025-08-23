package com.daemon.sweetsync.data.repository

import com.daemon.sweetsync.data.model.BloodSugarReading
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BloodSugarRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val authRepository: AuthRepository
) {
    suspend fun insertReading(reading: BloodSugarReading): Result<Unit> {
        return try {
            val userId = authRepository.getCurrentUser()?.id ?: throw Exception("User not logged in")
            val readingWithUser = reading.copy(user_id = userId)

            supabaseClient.client.from("blood_sugar_readings").insert(readingWithUser)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReadings(): Result<List<BloodSugarReading>> {
        return try {
            val userId = authRepository.getCurrentUser()?.id ?: throw Exception("User not logged in")

            val readings = supabaseClient.client.from("blood_sugar_readings")
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                    }
                    order("timestamp", Order.DESCENDING)
                }
                .decodeList<BloodSugarReading>()

            Result.success(readings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getReadingsFlow(): Flow<List<BloodSugarReading>> = flow {
        try {
            val result = getReadings()
            if (result.isSuccess) {
                emit(result.getOrNull() ?: emptyList())
            }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}

