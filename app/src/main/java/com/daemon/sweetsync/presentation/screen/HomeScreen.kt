package com.daemon.sweetsync.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.daemon.sweetsync.data.model.BloodSugarReading
import com.daemon.sweetsync.data.model.MealContext
import com.daemon.sweetsync.presentation.viewmodel.AuthViewModel
import com.daemon.sweetsync.presentation.viewmodel.BloodSugarViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.daemon.sweetsync.utils.DateTimeUtils
import kotlinx.coroutines.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog

// Enhanced Dark Theme Color Definitions
object AppColors {
    // Primary Colors (Dark Theme)
    val PrimaryBlue = Color(0xFF64B5F6)
    val PrimaryDark = Color(0xFF1565C0)
    val PrimaryLight = Color(0xFF90CAF9)

    // Secondary Colors
    val SecondaryTeal = Color(0xFF4DB6AC)
    val SecondaryTealDark = Color(0xFF00695C)
    val SecondaryTealLight = Color(0xFF80CBC4)

    // Dark Background Colors
    val BackgroundPrimary = Color(0xFF0F0F0F)      // Darker main background
    val BackgroundSecondary = Color(0xFF1A1A1A)    // Secondary dark background
    val BackgroundCard = Color(0xFF242424)         // Enhanced card background
    val BackgroundSurface = Color(0xFF1E1E1E)      // Surface background
    val BackgroundElevated = Color(0xFF2A2A2A)     // Elevated card background

    // Dark Theme Text Colors
    val TextPrimary = Color(0xFFF5F5F5)            // Brighter primary text
    val TextSecondary = Color(0xFFB8B8B8)          // Secondary text
    val TextHint = Color(0xFF808080)               // Hint text
    val TextOnPrimary = Color(0xFF000000)          // Text on primary colored backgrounds
    val TextOnSurface = Color(0xFFF0F0F0)          // Text on surface

    // Sugar Level Colors (Enhanced for dark theme)
    val SugarNormal = Color(0xFF66BB6A)
    val SugarHigh = Color(0xFFEF5350)
    val SugarLow = Color(0xFFFFCA28)

    // Meal Context Colors - Enhanced
    val BeforeMealBackground = Color(0xFF1B2A2E)
    val BeforeMealText = Color(0xFF4DD0E1)
    val AfterMealBackground = Color(0xFF1A1B2E)
    val AfterMealText = Color(0xFF64B5F6)

    // Status Colors
    val SuccessGreen = Color(0xFF66BB6A)
    val WarningOrange = Color(0xFFFFB74D)
    val ErrorRed = Color(0xFFEF5350)
    val InfoBlue = Color(0xFF64B5F6)


    // Card Border and Shadow
    val CardBorder = Color(0xFF333333)
    val CardShadow = Color(0x1A000000)

    // Dark Theme Neutral Colors
    val Grey300 = Color(0xFF2E2E2E)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    bloodSugarViewModel: BloodSugarViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToCharts: () -> Unit,
    onSignOut: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by bloodSugarViewModel.uiState.collectAsState()
    val authUiState by authViewModel.uiState.collectAsState()
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = false
    val listState = rememberLazyListState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // SwipeRefresh state
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = uiState.isRefreshing
    )

    // Error dialog state with delay
    var showErrorDialog by remember { mutableStateOf(false) }
    var showFullError by remember { mutableStateOf(false) }
    var hasInitialLoadCompleted by remember { mutableStateOf(false) }
    var showNoDataDialog by remember { mutableStateOf(false) }

    // Delay initial error display and data loading by 2 seconds after HomeScreen appears
    LaunchedEffect(Unit) {
        // Load cached data immediately
        bloodSugarViewModel.loadCachedReadings()
        kotlinx.coroutines.delay(2000) // 2 second delay
        hasInitialLoadCompleted = true

        // Trigger initial data refresh after delay (only if no data exists)
        if (uiState.readings.isEmpty() && !uiState.isLoading) {
            bloodSugarViewModel.refreshReadings()
        }
    }

    // Only show error dialog after initial delay AND if there's an error
    LaunchedEffect(uiState.errorMessage, hasInitialLoadCompleted) {
        if (uiState.errorMessage != null && hasInitialLoadCompleted) {
            showErrorDialog = true
            showFullError = false
        }
    }

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = AppColors.BackgroundPrimary,
            darkIcons = useDarkIcons
        )
    }

    // Load user profile when screen loads
    LaunchedEffect(Unit) {
        if (!authUiState.isAuthenticated) {
            authViewModel.checkAuthStatus()
        }
    }

    LaunchedEffect(authUiState.userProfile?.name) {
        authUiState.userProfile?.name?.let { userName ->
            bloodSugarViewModel.cacheUserName(userName)
        }
    }

    LaunchedEffect(authUiState.isAuthenticated) {
        if (!authUiState.isAuthenticated && !authUiState.isLoading) {
            onSignOut()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = { /* TODO: Navigate to Profile */ }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onSignOut()
                    }
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.BackgroundPrimary)
        ) {
            // Status Bar Spacer
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsTopHeight(WindowInsets.statusBars)
            )

            // Dedicated Top Bar
            DedicatedTopBar(
                userName = authUiState.userProfile?.name
                    ?: bloodSugarViewModel.getCachedUserName()
                    ?: "SweetSync",
                onNavigateToCharts = {
                    if (uiState.readings.isEmpty()) {
                        showNoDataDialog = true
                    } else {
                        onNavigateToCharts()
                    }
                },
                onMenuClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }
            )

            // Main Content
            Box(modifier = Modifier.weight(1f)) {
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = {
                        bloodSugarViewModel.refreshReadings()
                        authViewModel.checkAuthStatus()
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Header Section
                        item {
                            HeaderSectionCard(
                                readingsCount = uiState.readings.size,
                                isLoading = uiState.isLoading,
                                onRefresh = {
                                    bloodSugarViewModel.refreshReadings()
                                    authViewModel.checkAuthStatus()
                                }
                            )
                        }

                        // Stats Section
                        if (uiState.readings.isNotEmpty()) {
                            item {
                                StatsCard(readings = uiState.readings)
                            }
                        }

                        // Loading State
                        if (uiState.isLoading && !uiState.isRefreshing) {
                            item {
                                LoadingCard()
                            }
                        }

                        // Empty State
                        else if (uiState.readings.isEmpty() && !uiState.isLoading) {
                            item {
                                EmptyStateCard(onAddClick = onNavigateToAdd)
                            }
                        }

                        // Readings List
                        else {
                            items(uiState.readings) { reading ->
                                EnhancedReadingCard(reading = reading)
                            }
                        }
                    }
                }

                // FAB positioned at bottom right
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Card(
                        modifier = Modifier.clickable { onNavigateToAdd() },
                        colors = CardDefaults.cardColors(
                            containerColor = AppColors.SecondaryTeal
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier.padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add Reading",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Error Dialog (Enhanced) - Only show after delay
    if (showErrorDialog && uiState.errorMessage != null && hasInitialLoadCompleted) {
        ErrorDialog(
            errorMessage = uiState.errorMessage!!,
            showFullError = showFullError,
            onShowFullError = { showFullError = true },
            onDismiss = {
                showErrorDialog = false
                bloodSugarViewModel.clearError()
            },
            onRetry = {
                showErrorDialog = false
                bloodSugarViewModel.clearError()
                bloodSugarViewModel.refreshReadings()
                authViewModel.checkAuthStatus()
            }
        )
    }
    // No Data Dialog
    if (showNoDataDialog) {
        NoDataDialog(
            onDismiss = { showNoDataDialog = false },
            onAddReading = {
                showNoDataDialog = false
                onNavigateToAdd()
            }
        )
    }
}

@Composable
private fun DedicatedTopBar(
    userName: String,
    onNavigateToCharts: () -> Unit,
    onMenuClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 5.dp)
            .border(
                width = 2.dp,
                color = AppColors.BeforeMealBackground,
                shape = RoundedCornerShape(16.dp)
            ),
        color = AppColors.BackgroundCard,

        shadowElevation = 8.dp,
//                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Info Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = AppColors.TextPrimary
                    )
                }
                Column {
                    Text(
                        text = userName,
                        color = AppColors.TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Blood Sugar Tracker",
                        color = AppColors.TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Charts Button
                Card(
                    modifier = Modifier.clickable { onNavigateToCharts() },
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.SecondaryTeal.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Timeline,
                            contentDescription = "Charts",
                            tint = AppColors.SecondaryTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Charts",
                            color = AppColors.SecondaryTeal,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSectionCard(
    readingsCount: Int,
    isLoading: Boolean,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.BackgroundCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Recent Readings",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
                if (readingsCount > 0) {
                    Text(
                        text = "$readingsCount total readings",
                        fontSize = 14.sp,
                        color = AppColors.TextSecondary
                    )
                }
            }

            Card(
                modifier = Modifier.clickable { onRefresh() },
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.PrimaryBlue.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = AppColors.PrimaryBlue,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = AppColors.PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsCard(readings: List<BloodSugarReading>) {
    val avgGlucose = readings.map { it.glucose_level }.average()
    val normalCount = readings.count { it.glucose_level in 70.0..180.0 }
    val highCount = readings.count { it.glucose_level > 180.0 }
    val lowCount = readings.count { it.glucose_level < 70.0 }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.BackgroundCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Quick Stats",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Average",
                    value = "${avgGlucose.toInt()} mg/dL",
                    color = AppColors.InfoBlue
                )
                StatItem(
                    label = "Normal",
                    value = "$normalCount",
                    color = AppColors.SugarNormal
                )
                StatItem(
                    label = "High",
                    value = "$highCount",
                    color = AppColors.SugarHigh
                )
                StatItem(
                    label = "Low",
                    value = "$lowCount",
                    color = AppColors.SugarLow
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = AppColors.TextSecondary
            )
        }
    }
}

@Composable
private fun LoadingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.BackgroundCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = AppColors.PrimaryBlue
            )
        }
    }
}

@Composable
private fun EmptyStateCard(onAddClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.BackgroundCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.SecondaryTeal.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = AppColors.SecondaryTeal,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No readings yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Track your blood sugar levels to see trends and insights",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.clickable { onAddClick() },
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.SecondaryTeal
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Add First Reading",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun EnhancedReadingCard(reading: BloodSugarReading) {
    val (backgroundColor, textColor) = getMealContextColors(reading.getMealContextEnum())
    val glucoseColor = when {
        reading.glucose_level < 70 -> AppColors.SugarLow
        reading.glucose_level > 180 -> AppColors.SugarHigh
        else -> AppColors.SugarNormal
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, AppColors.CardBorder.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Glucose Level with Enhanced Styling
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = glucoseColor.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "${reading.glucose_level.toInt()} mg/dL",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = glucoseColor,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                // Meal Context Badge
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = textColor.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = reading.meal_context.replace("_", " "),
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Timestamp with Icon
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = textColor.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = DateTimeUtils.formatDateTime(reading.timestamp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor.copy(alpha = 0.8f)
                )
            }

            // Notes Section (Enhanced)
            reading.notes?.let { notes ->
                if (notes != "null" && notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        border = BorderStroke(1.dp, textColor.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Notes: $notes",
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor.copy(alpha = 0.7f),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun NoDataDialog(
    onDismiss: () -> Unit,
    onAddReading: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.BackgroundCard
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.WarningOrange.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        Icons.Default.Timeline,
                        contentDescription = null,
                        tint = AppColors.WarningOrange,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Add a reading first",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "You need to add at least one blood sugar reading before viewing charts and trends.",
                    fontSize = 14.sp,
                    color = AppColors.TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Cancel Button
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onDismiss() },
                        colors = CardDefaults.cardColors(
                            containerColor = AppColors.BackgroundSecondary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Cancel",
                                fontWeight = FontWeight.Medium,
                                color = AppColors.TextSecondary,
                                fontSize = 14.sp,
                                maxLines = 1,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    // Add Reading Button
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onAddReading() },
                        colors = CardDefaults.cardColors(
                            containerColor = AppColors.SecondaryTeal
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Add Reading",
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                maxLines = 1,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorDialog(
    errorMessage: String,
    showFullError: Boolean,
    onShowFullError: () -> Unit,
    onDismiss: () -> Unit,
    onRetry: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppColors.BackgroundCard
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.ErrorRed.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = AppColors.ErrorRed,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Connection Error",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (showFullError) {
                        errorMessage
                    } else {
                        "Unable to connect to server. Please check your internet connection and try again."
                    },
                    fontSize = 14.sp,
                    color = AppColors.TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (!showFullError) Arrangement.spacedBy(8.dp) else Arrangement.Center
                ) {
                    if (!showFullError) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onShowFullError() },
                            // ... existing card properties
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Details",
                                    fontWeight = FontWeight.Medium,
                                    color = AppColors.TextSecondary,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .let { if (!showFullError) it.weight(1f) else it.fillMaxWidth() }
                            .clickable { onRetry() },
                        // ... existing card properties
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Retry",
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getMealContextColors(mealContext: MealContext): Pair<Color, Color> {
    return when (mealContext) {
        MealContext.BEFORE_MEAL -> Pair(
            AppColors.BeforeMealBackground,
            AppColors.BeforeMealText
        )
        MealContext.AFTER_MEAL -> Pair(
            AppColors.AfterMealBackground,
            AppColors.AfterMealText
        )
    }
}