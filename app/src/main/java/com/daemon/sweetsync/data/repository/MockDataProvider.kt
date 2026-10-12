package com.daemon.sweetsync.data.repository

import com.daemon.sweetsync.data.model.FamilyMember
import com.daemon.sweetsync.data.model.HealthRecord
import com.daemon.sweetsync.data.model.StatusLevel
import com.daemon.sweetsync.data.model.TrackType

object MockDataProvider {

    val defaultMembers = listOf(
        FamilyMember(
            id = "mem_robert_1",
            fullName = "Robert Miller",
            relationship = "Parent",
            age = 62,
            gender = "Male",
            avatarDrawableName = "avatar_robert",
            activeTracks = listOf(TrackType.BLOOD_SUGAR.code, TrackType.BLOOD_PRESSURE.code),
            remindersEnabled = mapOf("fasting" to true, "post_lunch" to true),
            lastLoggedText = "Logged 8:15 AM Today",
            deviceSyncName = "Omron i-C10"
        ),
        FamilyMember(
            id = "mem_elena_2",
            fullName = "Elena Miller",
            relationship = "Parent",
            age = 58,
            gender = "Female",
            avatarDrawableName = "avatar_elena",
            activeTracks = listOf(TrackType.BLOOD_SUGAR.code, TrackType.BLOOD_PRESSURE.code, TrackType.WEIGHT.code),
            remindersEnabled = mapOf("fasting" to true, "post_lunch" to true),
            lastLoggedText = "Logged 8:00 AM Today",
            deviceSyncName = "Accu-Chek Guide"
        ),
        FamilyMember(
            id = "mem_lucas_3",
            fullName = "Lucas Miller",
            relationship = "Child",
            age = 19,
            gender = "Male",
            avatarDrawableName = "avatar_lucas",
            activeTracks = listOf(TrackType.WEIGHT.code),
            remindersEnabled = mapOf("fasting" to true, "post_lunch" to false),
            lastLoggedText = "Logged 2:15 PM Today",
            deviceSyncName = "Withings Body Scan"
        ),
        FamilyMember(
            id = "mem_sarah_4",
            fullName = "Sarah Miller",
            relationship = "Self",
            age = 32,
            gender = "Female",
            avatarDrawableName = "avatar_sarah",
            activeTracks = listOf(TrackType.BLOOD_SUGAR.code, TrackType.BLOOD_PRESSURE.code, TrackType.WEIGHT.code),
            remindersEnabled = mapOf("fasting" to true, "post_lunch" to true),
            lastLoggedText = "All Daily Targets Complete",
            deviceSyncName = "Apple Health"
        )
    )

    val defaultRecords = listOf(
        // TODAY - OCT 24, 2024
        // Robert Miller - Blood Glucose
        HealthRecord(
            id = "rec_sugar_1",
            memberId = "mem_robert_1",
            memberName = "Robert Miller",
            trackType = TrackType.BLOOD_SUGAR.code,
            timestamp = 1729759800000L, // 7:45 AM
            dateString = "2024-10-24",
            timeString = "7:45 AM",
            sugarFasting = 104.0,
            sugarPostLunch = 138.0,
            mealTag = "Fasting (8+ hrs)",
            mealNote = "After grilled chicken salad & sparkling water",
            statusText = "Target Normal",
            statusLevel = StatusLevel.OPTIMAL
        ),
        HealthRecord(
            id = "rec_sugar_2",
            memberId = "mem_robert_1",
            memberName = "Robert Miller",
            trackType = TrackType.BLOOD_SUGAR.code,
            timestamp = 1729783800000L, // 2:10 PM
            dateString = "2024-10-24",
            timeString = "2:10 PM",
            sugarPostLunch = 138.0,
            mealTag = "Post-Lunch",
            mealNote = "After grilled chicken salad & sparkling water",
            statusText = "Normal",
            statusLevel = StatusLevel.NORMAL
        ),
        // Elena Miller - Blood Sugar
        HealthRecord(
            id = "rec_sugar_elena_1",
            memberId = "mem_elena_2",
            memberName = "Elena Miller",
            trackType = TrackType.BLOOD_SUGAR.code,
            timestamp = 1729760400000L, // 8:00 AM
            dateString = "2024-10-24",
            timeString = "8:00 AM",
            sugarFasting = 96.0,
            mealTag = "Fasting",
            statusText = "Target Optimal",
            statusLevel = StatusLevel.OPTIMAL
        ),
        // Sarah Miller - Blood Sugar
        HealthRecord(
            id = "rec_sugar_sarah_1",
            memberId = "mem_sarah_4",
            memberName = "Sarah Miller",
            trackType = TrackType.BLOOD_SUGAR.code,
            timestamp = 1729758000000L,
            dateString = "2024-10-24",
            timeString = "7:15 AM",
            sugarFasting = 92.0,
            mealTag = "Fasting",
            statusText = "In Target",
            statusLevel = StatusLevel.OPTIMAL
        ),

        // Robert Miller - Blood Pressure
        HealthRecord(
            id = "rec_bp_robert_1",
            memberId = "mem_robert_1",
            memberName = "Robert Miller",
            trackType = TrackType.BLOOD_PRESSURE.code,
            timestamp = 1729761300000L, // 8:15 AM
            dateString = "2024-10-24",
            timeString = "8:15 AM",
            systolic = 124,
            diastolic = 82,
            heartRate = 72,
            postureContext = "Left Arm • Sitting • Resting 5 min",
            bpTag = "Morning Baseline • Left Arm",
            statusText = "Optimal",
            statusLevel = StatusLevel.OPTIMAL
        ),
        // Elena Miller - Blood Pressure
        HealthRecord(
            id = "rec_bp_elena_1",
            memberId = "mem_elena_2",
            memberName = "Elena Miller",
            trackType = TrackType.BLOOD_PRESSURE.code,
            timestamp = 1729762200000L, // 8:30 AM
            dateString = "2024-10-24",
            timeString = "8:30 AM",
            systolic = 118,
            diastolic = 78,
            heartRate = 68,
            postureContext = "Right Arm • Sitting",
            bpTag = "Post-Meditation • Right Arm",
            statusText = "Normal",
            statusLevel = StatusLevel.NORMAL
        ),
        // Sarah Miller - Blood Pressure
        HealthRecord(
            id = "rec_bp_sarah_1",
            memberId = "mem_sarah_4",
            memberName = "Sarah Miller",
            trackType = TrackType.BLOOD_PRESSURE.code,
            timestamp = 1729759000000L,
            dateString = "2024-10-24",
            timeString = "7:30 AM",
            systolic = 116,
            diastolic = 74,
            heartRate = 65,
            bpTag = "Morning Check",
            statusText = "Optimal",
            statusLevel = StatusLevel.OPTIMAL
        ),

        // Elena Miller - Weight
        HealthRecord(
            id = "rec_wt_elena_1",
            memberId = "mem_elena_2",
            memberName = "Elena Miller",
            trackType = TrackType.WEIGHT.code,
            timestamp = 1729760400000L,
            dateString = "2024-10-24",
            timeString = "8:00 AM",
            weightFasting = 64.2,
            unit = "kg",
            weightDeltaVsYesterday = "-0.4 kg",
            statusText = "Normal",
            statusLevel = StatusLevel.NORMAL
        ),
        // Lucas Miller - Weight
        HealthRecord(
            id = "rec_wt_lucas_1",
            memberId = "mem_lucas_3",
            memberName = "Lucas Miller",
            trackType = TrackType.WEIGHT.code,
            timestamp = 1729784100000L, // 2:15 PM
            dateString = "2024-10-24",
            timeString = "2:15 PM",
            weightFasting = 72.8,
            weightPostLunch = 73.5,
            unit = "kg",
            contextTags = listOf("Post-workout", "Well hydrated"),
            statusText = "Active Athlete",
            statusLevel = StatusLevel.INFO
        ),
        // Sarah Miller - Weight
        HealthRecord(
            id = "rec_wt_sarah_1",
            memberId = "mem_sarah_4",
            memberName = "Sarah Miller",
            trackType = TrackType.WEIGHT.code,
            timestamp = 1729757700000L, // 7:15 AM
            dateString = "2024-10-24",
            timeString = "07:15 AM",
            weightFasting = 64.2,
            weightPostLunch = 64.9,
            unit = "kg",
            contextTags = listOf("Post morning walk", "Well hydrated"),
            weightDeltaVsYesterday = "-0.2 kg vs yesterday",
            statusText = "Primary",
            statusLevel = StatusLevel.OPTIMAL
        ),

        // YESTERDAY - OCT 23, 2024
        HealthRecord(
            id = "rec_sugar_robert_y1",
            memberId = "mem_robert_1",
            memberName = "Robert Miller",
            trackType = TrackType.BLOOD_SUGAR.code,
            timestamp = 1729690200000L,
            dateString = "2024-10-23",
            timeString = "1:50 PM",
            sugarPostLunch = 142.0,
            mealTag = "Post-Lunch",
            mealNote = "Quinoa bowl",
            statusText = "Normal",
            statusLevel = StatusLevel.NORMAL
        ),
        HealthRecord(
            id = "rec_sugar_robert_y2",
            memberId = "mem_robert_1",
            memberName = "Robert Miller",
            trackType = TrackType.BLOOD_SUGAR.code,
            timestamp = 1729667400000L,
            dateString = "2024-10-23",
            timeString = "7:30 AM",
            sugarFasting = 101.0,
            mealTag = "Fasting",
            statusText = "Normal",
            statusLevel = StatusLevel.NORMAL
        ),
        HealthRecord(
            id = "rec_bp_robert_y1",
            memberId = "mem_robert_1",
            memberName = "Robert Miller",
            trackType = TrackType.BLOOD_PRESSURE.code,
            timestamp = 1729711800000L, // 7:50 PM
            dateString = "2024-10-23",
            timeString = "7:50 PM",
            systolic = 128,
            diastolic = 84,
            heartRate = 75,
            bpTag = "Evening Relax • Left Arm",
            statusText = "Normal",
            statusLevel = StatusLevel.NORMAL
        ),
        HealthRecord(
            id = "rec_bp_robert_y2",
            memberId = "mem_robert_1",
            memberName = "Robert Miller",
            trackType = TrackType.BLOOD_PRESSURE.code,
            timestamp = 1729669200000L, // 8:00 AM
            dateString = "2024-10-23",
            timeString = "8:00 AM",
            systolic = 122,
            diastolic = 80,
            heartRate = 70,
            bpTag = "Morning Routine • Left Arm",
            statusText = "Optimal",
            statusLevel = StatusLevel.OPTIMAL
        ),
        HealthRecord(
            id = "rec_wt_sarah_y1",
            memberId = "mem_sarah_4",
            memberName = "Sarah Miller",
            trackType = TrackType.WEIGHT.code,
            timestamp = 1729689900000L,
            dateString = "2024-10-23",
            timeString = "01:45 PM",
            weightFasting = 64.4,
            weightPostLunch = 65.1,
            unit = "kg",
            statusText = "Baseline",
            statusLevel = StatusLevel.NORMAL
        ),
        HealthRecord(
            id = "rec_wt_elena_y1",
            memberId = "mem_elena_2",
            memberName = "Elena Miller",
            trackType = TrackType.WEIGHT.code,
            timestamp = 1729667400000L,
            dateString = "2024-10-23",
            timeString = "07:30 AM",
            weightFasting = 64.6,
            unit = "kg",
            statusText = "Optimal",
            statusLevel = StatusLevel.OPTIMAL
        ),

        // Additional days for multi-track calendar dots across October 2024
        // Oct 1: Sugar
        HealthRecord(id = "c1", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-01"),
        // Oct 2: Sugar, BP
        HealthRecord(id = "c2_1", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-02"),
        HealthRecord(id = "c2_2", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-02"),
        // Oct 3: Sugar
        HealthRecord(id = "c3", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-03"),
        // Oct 4: BP
        HealthRecord(id = "c4", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-04"),
        // Oct 5: Weight
        HealthRecord(id = "c5", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.WEIGHT.code, dateString = "2024-10-05"),
        // Oct 6: Sugar, BP
        HealthRecord(id = "c6_1", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-06"),
        HealthRecord(id = "c6_2", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-06"),
        // Oct 7: Sugar
        HealthRecord(id = "c7", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-07"),
        // Oct 8: Sugar, BP
        HealthRecord(id = "c8_1", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-08"),
        HealthRecord(id = "c8_2", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-08"),
        // Oct 9: BP
        HealthRecord(id = "c9", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-09"),
        // Oct 10: Sugar
        HealthRecord(id = "c10", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-10"),
        // Oct 11: Sugar, BP, Weight
        HealthRecord(id = "c11_1", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-11"),
        HealthRecord(id = "c11_2", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-11"),
        HealthRecord(id = "c11_3", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.WEIGHT.code, dateString = "2024-10-11"),
        // Oct 12: BP
        HealthRecord(id = "c12", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-12"),
        // Oct 13: Sugar
        HealthRecord(id = "c13", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-13"),
        // Oct 14: Sugar, BP
        HealthRecord(id = "c14_1", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-14"),
        HealthRecord(id = "c14_2", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-14"),
        // Oct 15: Sugar
        HealthRecord(id = "c15", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-15"),
        // Oct 16: BP
        HealthRecord(id = "c16", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-16"),
        // Oct 17: Sugar, BP
        HealthRecord(id = "c17_1", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-17"),
        HealthRecord(id = "c17_2", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-17"),
        // Oct 18: Sugar, Weight
        HealthRecord(id = "c18_1", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-18"),
        HealthRecord(id = "c18_2", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.WEIGHT.code, dateString = "2024-10-18"),
        // Oct 19: BP
        HealthRecord(id = "c19", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-19"),
        // Oct 20: Sugar
        HealthRecord(id = "c20", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-20"),
        // Oct 21: Sugar, BP
        HealthRecord(id = "c21_1", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-21"),
        HealthRecord(id = "c21_2", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-21"),
        // Oct 22: BP
        HealthRecord(id = "c22", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-22"),
        // Oct 23: Sugar
        HealthRecord(id = "c23", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-23"),
        // Oct 24: Sugar, BP, Weight (Today!)
        HealthRecord(id = "c24_1", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-24"),
        HealthRecord(id = "c24_2", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-24"),
        HealthRecord(id = "c24_3", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.WEIGHT.code, dateString = "2024-10-24"),
        // Oct 25: Sugar
        HealthRecord(id = "c25", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-25"),
        // Oct 26: BP
        HealthRecord(id = "c26", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_PRESSURE.code, dateString = "2024-10-26"),
        // Oct 27: Sugar
        HealthRecord(id = "c27", memberId = "mem_robert_1", memberName = "Robert Miller", trackType = TrackType.BLOOD_SUGAR.code, dateString = "2024-10-27")
    )
}
