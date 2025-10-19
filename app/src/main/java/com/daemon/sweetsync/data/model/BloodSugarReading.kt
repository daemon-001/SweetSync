package com.daemon.sweetsync.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BloodSugarReading(
    @DocumentId
    val id: String? = null,
    
    @PropertyName("user_id")
    val user_id: String = "",
    
    @PropertyName("glucose_level")
    val glucose_level: Double = 0.0,
    
    @PropertyName("timestamp")
    val timestamp: Long = 0L, // Changed to Long for Firebase Timestamp
    
    @PropertyName("notes")
    val notes: String? = null,
    
    @PropertyName("meal_context")
    val meal_context: String = MealContext.BEFORE_MEAL.name // Store as String for Firebase
) {
    // No-arg constructor for Firebase
    constructor() : this(null, "", 0.0, 0L, null, MealContext.BEFORE_MEAL.name)
    
    // Helper to convert to MealContext enum
    fun getMealContextEnum(): MealContext {
        return try {
            MealContext.valueOf(meal_context)
        } catch (e: Exception) {
            MealContext.BEFORE_MEAL
        }
    }
}

@Serializable
enum class MealContext {
    @SerialName("BEFORE_MEAL")
    BEFORE_MEAL,

    @SerialName("AFTER_MEAL")
    AFTER_MEAL
}

