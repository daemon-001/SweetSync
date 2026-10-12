package com.sweetsync.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sweetsync.app.data.repository.AuthRepository
import com.sweetsync.app.data.repository.HealthRepository
import com.sweetsync.app.ui.components.SweetSyncBottomBar
import com.sweetsync.app.ui.screens.account.AccountScreen
import com.sweetsync.app.ui.screens.account.AccountViewModel
import com.sweetsync.app.ui.screens.addmember.AddMemberScreen
import com.sweetsync.app.ui.screens.addmember.AddMemberViewModel
import com.sweetsync.app.ui.screens.bloodpressure.BloodPressureScreen
import com.sweetsync.app.ui.screens.bloodpressure.BloodPressureViewModel
import com.sweetsync.app.ui.screens.bloodsugar.BloodSugarScreen
import com.sweetsync.app.ui.screens.bloodsugar.BloodSugarViewModel
import com.sweetsync.app.ui.screens.home.HomeScreen
import com.sweetsync.app.ui.screens.home.HomeViewModel
import com.sweetsync.app.ui.screens.memberdetails.MemberDetailScreen
import com.sweetsync.app.ui.screens.memberdetails.MemberDetailViewModel
import com.sweetsync.app.ui.screens.weight.WeightScreen
import com.sweetsync.app.ui.screens.weight.WeightViewModel

import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun SweetSyncApp(
    authRepository: AuthRepository,
    healthRepository: HealthRepository,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    Scaffold(
        bottomBar = {
            SweetSyncBottomBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    val targetRoute = when (route) {
                        "home" -> Screen.Home.route
                        "add_member" -> Screen.AddMember.route
                        "records_menu" -> Screen.BloodSugar.route
                        "account" -> Screen.Account.route
                        else -> route
                    }
                    navController.navigate(targetRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                val homeViewModel = remember { HomeViewModel(healthRepository, authRepository) }
                HomeScreen(
                    viewModel = homeViewModel,
                    onNavigateToMemberDetail = { memberId ->
                        navController.navigate(Screen.MemberDetail.createRoute(memberId))
                    },
                    onNavigateToTrack = { track ->
                        when (track) {
                            "blood_sugar" -> navController.navigate(Screen.BloodSugar.route)
                            "blood_pressure" -> navController.navigate(Screen.BloodPressure.route)
                            "weight" -> navController.navigate(Screen.Weight.route)
                        }
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.Account.route)
                    }
                )
            }

            composable(Screen.AddMember.route) {
                val addMemberViewModel = remember { AddMemberViewModel(healthRepository) }
                AddMemberScreen(
                    viewModel = addMemberViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onMemberSaved = { memberId ->
                        navController.navigate(Screen.MemberDetail.createRoute(memberId)) {
                            popUpTo(Screen.Home.route)
                        }
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.Account.route)
                    }
                )
            }

            composable(
                route = Screen.MemberDetail.route,
                arguments = listOf(navArgument("memberId") { type = NavType.StringType })
            ) { backStackEntry ->
                val memberId = backStackEntry.arguments?.getString("memberId") ?: "mem_robert_1"
                val memberDetailViewModel = remember(memberId) {
                    MemberDetailViewModel(memberId, healthRepository)
                }
                MemberDetailScreen(
                    viewModel = memberDetailViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToTrack = { track ->
                        when (track) {
                            "blood_sugar" -> navController.navigate(Screen.BloodSugar.route)
                            "blood_pressure" -> navController.navigate(Screen.BloodPressure.route)
                            "weight" -> navController.navigate(Screen.Weight.route)
                        }
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.Account.route)
                    }
                )
            }

            composable(Screen.BloodSugar.route) {
                val sugarViewModel = remember { BloodSugarViewModel(healthRepository) }
                BloodSugarScreen(
                    viewModel = sugarViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToProfile = { navController.navigate(Screen.Account.route) }
                )
            }

            composable(Screen.BloodPressure.route) {
                val bpViewModel = remember { BloodPressureViewModel(healthRepository) }
                BloodPressureScreen(
                    viewModel = bpViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToProfile = { navController.navigate(Screen.Account.route) }
                )
            }

            composable(Screen.Weight.route) {
                val weightViewModel = remember { WeightViewModel(healthRepository) }
                WeightScreen(
                    viewModel = weightViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToProfile = { navController.navigate(Screen.Account.route) }
                )
            }

            composable(Screen.Account.route) {
                val accountViewModel = remember { AccountViewModel(authRepository, healthRepository) }
                AccountScreen(
                    viewModel = accountViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
