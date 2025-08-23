package com.daemon.sweetsync.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BloodSugarReading(
    val id: String? = null,
    val user_id: String,
    val glucose_level: Double,
    val timestamp: String,
    val notes: String? = null,
    val meal_context: MealContext
)

@Serializable
enum class MealContext {
    @SerialName("BEFORE_MEAL")
    BEFORE_MEAL,

    @SerialName("AFTER_MEAL")
    AFTER_MEAL
}

