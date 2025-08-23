// Fixed SweetSyncNavigation.kt with proper logout handling
package com.daemon.sweetsync.presentation.navigation

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daemon.sweetsync.presentation.screen.*
import com.daemon.sweetsync.presentation.viewmodel.AuthViewModel
import com.daemon.sweetsync.presentation.viewmodel.BloodSugarViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Define colors matching the home screen theme
object AppColors {
    val BackgroundPrimary = Color(0xFF1A1A1A)
    val BackgroundSecondary = Color(0xFF2A2A2A)
    val CardBackground = Color(0xFF333333)
    val TealPrimary = Color(0xFF4DD0E1)
    val TealSecondary = Color(0xFF26C6DA)
    val TextPrimary = Color.White
    val TextSecondary = Color(0xFFB0B0B0)
    val SuccessGreen = Color(0xFF4CAF50)
    val ErrorRed = Color(0xFFFF5252)
    val WarningOrange = Color(0xFFFF9800)
}

@Composable
fun SweetSyncNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authUiState by authViewModel.uiState.collectAsState()

    // Create a shared BloodSugarViewModel at navigation level
    val bloodSugarViewModel: BloodSugarViewModel = hiltViewModel()

    // Check auth status only once when the app starts
    LaunchedEffect(Unit) {
        authViewModel.checkAuthStatus()
    }

    // Handle navigation based on authentication state changes
    LaunchedEffect(authUiState.isAuthenticated, authUiState.isLoading) {
        if (!authUiState.isLoading) {
            val currentRoute = navController.currentDestination?.route

            if (authUiState.isAuthenticated) {
                // User is authenticated - navigate to home if not already there
                if (currentRoute != "home") {
                    // Load cached data first for instant display
                    bloodSugarViewModel.loadCachedReadings()

                    // Cache user name if available
                    authUiState.userProfile?.name?.let { name ->
                        bloodSugarViewModel.cacheUserName(name)
                    }

                    // Navigate to home screen
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            } else {
                // User is not authenticated - navigate to auth if not already there
                if (currentRoute != "auth") {
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }

    // Show loading screen while checking auth status
    if (authUiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.BackgroundPrimary),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = AppColors.TealPrimary,
                strokeWidth = 3.dp
            )
        }
    } else {
        NavHost(
            navController = navController,
            startDestination = if (authUiState.isAuthenticated) "home" else "auth"
        ) {
            composable("auth") {
                AuthScreen(
                    onAuthSuccess = {
                        // The LaunchedEffect above will handle navigation
                        // Just refresh the auth status
                        authViewModel.checkAuthStatus()
                    }
                )
            }

            composable("home") {
                // Auto-refresh data when navigating to home screen
                LaunchedEffect(Unit) {
                    bloodSugarViewModel.refreshReadings()
                }

                HomeScreen(
                    bloodSugarViewModel = bloodSugarViewModel,
                    onNavigateToAdd = {
                        navController.navigate("add_reading")
                    },
                    onNavigateToCharts = {
                        navController.navigate("charts")
                    },
                    onSignOut = {
                        // Clear all data and sign out
                        bloodSugarViewModel.clearData()
                        authViewModel.signOut()
                        // Navigation will be handled automatically by LaunchedEffect above
                        // when authUiState.isAuthenticated becomes false
                    }
                )
            }

            composable("add_reading") {
                AddReadingScreen(
                    viewModel = bloodSugarViewModel,
                    onNavigateBack = { success ->
                        navController.popBackStack()
                        if (success) {
                            // Refresh data after successful addition
                            bloodSugarViewModel.refreshReadings()
                        }
                    }
                )
            }

            composable("charts") {
                ChartsScreen(
                    bloodSugarViewModel = bloodSugarViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}