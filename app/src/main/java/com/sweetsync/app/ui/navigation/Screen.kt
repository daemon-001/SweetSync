package com.sweetsync.app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddMember : Screen("add_member")
    object MemberDetail : Screen("member_detail/{memberId}") {
        fun createRoute(memberId: String) = "member_detail/$memberId"
    }
    object BloodSugar : Screen("blood_sugar")
    object BloodPressure : Screen("blood_pressure")
    object Weight : Screen("weight")
    object Account : Screen("account")
}
