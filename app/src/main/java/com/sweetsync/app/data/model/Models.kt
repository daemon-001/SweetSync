package com.sweetsync.app.data.model

enum class TrackType(val code: String, val displayName: String) {
    BLOOD_SUGAR("BLOOD_SUGAR", "Blood Sugar"),
    BLOOD_PRESSURE("BLOOD_PRESSURE", "Blood Pressure"),
    WEIGHT("WEIGHT", "Weight & BMI")
}

enum class StatusLevel {
    OPTIMAL,
    NORMAL,
    WARNING,
    INFO
}

data class UserProfile(
    val userId: String = "guest_sarah_101",
    val displayName: String = "Sarah Miller",
    val email: String = "sarah.miller@sweetsync.health",
    val isGuest: Boolean = false,
    val photoUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class FamilyMember(
    val id: String = "",
    val fullName: String = "",
    val relationship: String = "Parent", // "Parent", "Spouse", "Child", "Sibling", "Self", "Other"
    val age: Int = 0,
    val gender: String = "Male", // "Male", "Female"
    val photoUrl: String? = null,
    val avatarDrawableName: String = "", // "avatar_robert", "avatar_elena", "avatar_lucas", "avatar_sarah"
    val activeTracks: List<String> = listOf("BLOOD_SUGAR", "BLOOD_PRESSURE", "WEIGHT"),
    val remindersEnabled: Map<String, Boolean> = mapOf("fasting" to true, "post_lunch" to true),
    val lastLoggedText: String = "Logged 8:15 AM Today",
    val deviceSyncName: String? = "Omron i-C10",
    val doctorNotes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    val relationshipDisplay: String
        get() = when {
            relationship == "Self" -> "Self (Primary)"
            relationship == "Parent" && gender == "Male" -> "Father • ${age}y"
            relationship == "Parent" && gender == "Female" -> "Mother • ${age}y"
            relationship == "Child" && gender == "Male" -> "Son • ${age}y"
            relationship == "Child" && gender == "Female" -> "Daughter • ${age}y"
            else -> "$relationship • ${age}y"
        }

    val initials: String
        get() = fullName.split(" ").filter { it.isNotBlank() }.map { it.first() }.take(2).joinToString("").uppercase()
}

data class HealthRecord(
    val id: String = "",
    val memberId: String = "",
    val memberName: String = "",
    val trackType: String = "", // "BLOOD_SUGAR", "BLOOD_PRESSURE", "WEIGHT"
    val timestamp: Long = System.currentTimeMillis(),
    val dateString: String = "2024-10-24", // "yyyy-MM-dd"
    val timeString: String = "07:45 AM",
    
    // Blood Sugar
    val sugarFasting: Double? = null,
    val sugarPostLunch: Double? = null,
    val mealTag: String? = null, // "Before Breakfast", "Post-Lunch", "Fasting (8+ hrs)", "Bedtime"
    val mealNote: String? = null,
    
    // Blood Pressure
    val systolic: Int? = null,
    val diastolic: Int? = null,
    val heartRate: Int? = null,
    val postureContext: String? = null, // "Left Arm • Sitting • Resting 5 min"
    val bpTag: String? = null, // "Morning Baseline • Left Arm", "Post-Meditation • Right Arm"
    
    // Weight
    val weightFasting: Double? = null,
    val weightPostLunch: Double? = null,
    val unit: String = "kg",
    val contextTags: List<String> = emptyList(), // "Post morning walk", "Well hydrated"
    val weightDeltaVsYesterday: String? = null, // "-0.2 kg vs yesterday"
    
    // Status
    val statusText: String = "Normal",
    val statusLevel: StatusLevel = StatusLevel.NORMAL
)

data class GlycemicDataPoint(
    val dayLabel: String,
    val fastingValue: Double,
    val postLunchValue: Double
)
