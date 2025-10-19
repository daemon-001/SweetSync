package com.daemon.sweetsync.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daemon.sweetsync.data.model.BloodSugarReading
import com.daemon.sweetsync.data.model.MealContext
import com.daemon.sweetsync.data.repository.BloodSugarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

import android.content.Context
import android.content.SharedPreferences
//import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.core.content.edit

@HiltViewModel
class BloodSugarViewModel @Inject constructor(
    private val bloodSugarRepository: BloodSugarRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(BloodSugarUiState())
    val uiState: StateFlow<BloodSugarUiState> = _uiState.asStateFlow()

    init {
        loadReadings()
    }

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("blood_sugar_cache", Context.MODE_PRIVATE)
    }
    private val gson = Gson()
    private val cacheKey = "cached_readings"
    private val userNameCacheKey = "cached_username"

    fun addReading(glucoseLevel: Double, notes: String, mealContext: MealContext) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val reading = BloodSugarReading(
                user_id = "", // Will be set in repository
                glucose_level = glucoseLevel,
                timestamp = System.currentTimeMillis(), // Firebase uses milliseconds (Long)
                notes = notes.takeIf { it.isNotBlank() },
                meal_context = mealContext.name // Firebase stores enum as String
            )

            val result = bloodSugarRepository.insertReading(reading)

            if (result.isSuccess) {
                loadReadings() // This will refresh and cache the updated list
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Failed to add reading"
                )
            }
        }
    }
    fun cacheUserName(userName: String) {
        try {
            sharedPreferences.edit {
                putString(userNameCacheKey, userName)
            }
        } catch (e: Exception) {
            // Cache save failed, but don't interrupt the flow
        }
    }

    fun getCachedUserName(): String? {
        return try {
            sharedPreferences.getString(userNameCacheKey, null)
        } catch (e: Exception) {
            null
        }
    }

    fun loadCachedReadings() {
        viewModelScope.launch {
            try {
                val cachedJson = sharedPreferences.getString(cacheKey, null)
                if (cachedJson != null) {
                    val type = object : TypeToken<List<BloodSugarReading>>() {}.type
                    val cachedReadings: List<BloodSugarReading> = gson.fromJson(cachedJson, type)

                    _uiState.update { currentState ->
                        currentState.copy(
                            readings = cachedReadings,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                // If cache loading fails, just continue without cached data
            }
        }
    }

    private fun cacheReadings(readings: List<BloodSugarReading>) {
        try {
            val json = gson.toJson(readings)
            sharedPreferences.edit {
                putString(cacheKey, json)
            }
        } catch (e: Exception) {
            // Cache save failed, but don't interrupt the flow
        }
    }

    private fun loadReadings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = bloodSugarRepository.getReadings()

            _uiState.value = if (result.isSuccess) {
                val readings = result.getOrNull() ?: emptyList()
                // Cache successful data
                cacheReadings(readings)

                _uiState.value.copy(
                    isLoading = false,
                    readings = readings,
                    errorMessage = null
                )
            } else {
                // On failure, try to load cached data
                loadCachedReadings()

                _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Failed to load readings"
                )
            }
        }
    }

    fun refreshReadings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)

            val result = bloodSugarRepository.getReadings()

            _uiState.value = if (result.isSuccess) {
                val readings = result.getOrNull() ?: emptyList()
                // Cache successful data
                cacheReadings(readings)

                _uiState.value.copy(
                    isRefreshing = false,
                    readings = readings,
                    errorMessage = null
                )
            } else {
                // On failure during refresh, keep existing data and show error
                // Don't reload cache during refresh as user expects fresh data
                _uiState.value.copy(
                    isRefreshing = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Failed to refresh readings"
                )
            }
        }
    }
    fun clearError() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }

    fun clearData() {
        viewModelScope.launch {
            // Clear cached data
            try {
                sharedPreferences.edit {
                    remove(cacheKey)
                    remove(userNameCacheKey)
                }
            } catch (e: Exception) {
                // Cache clear failed, but continue
            }

            // Clear UI state
            _uiState.update { currentState ->
                currentState.copy(
                    readings = emptyList(),
                    errorMessage = null,
                    isLoading = false,
                    isRefreshing = false
                )
            }
        }
    }


}

data class BloodSugarUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val readings: List<BloodSugarReading> = emptyList(),
    val errorMessage: String? = null
)