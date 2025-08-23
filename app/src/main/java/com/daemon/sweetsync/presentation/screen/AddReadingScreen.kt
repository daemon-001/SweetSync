package com.daemon.sweetsync.presentation.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.daemon.sweetsync.data.model.MealContext
import com.daemon.sweetsync.presentation.viewmodel.BloodSugarViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

// Dark Theme Colors to match home screen
object DarkAppColors {
    val BackgroundPrimary = Color(0xFF0A0A0A)
    val BackgroundSecondary = Color(0xFF121212)
    val BackgroundCard = Color(0xFF1E1E1E)
    val BackgroundSurface = Color(0xFF2A2A2A)

    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFB0B0B0)
    val TextOnSurface = Color(0xFFFFFFFF)
    val TextOnPrimary = Color(0xFFFFFFFF)
    val TextHint = Color(0xFF666666)

    val PrimaryBlue = Color(0xFF4A90E2)
    val SecondaryTeal = Color(0xFF5CB3A8)
    val SuccessGreen = Color(0xFF4CAF50)
    val ErrorRed = Color(0xFFE53E3E)
    val WarningOrange = Color(0xFFFF9500)

    val SugarNormal = Color(0xFF4CAF50)
    val SugarHigh = Color(0xFFE53E3E)
    val SugarLow = Color(0xFFFF9500)

    val BeforeMealText = Color(0xFF4A90E2)
    val AfterMealText = Color(0xFF5CB3A8)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AddReadingScreen(
    onNavigateBack: (success: Boolean) -> Unit,
    viewModel: BloodSugarViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var glucoseLevel by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedMealContext by remember { mutableStateOf(MealContext.BEFORE_MEAL) }
    var expanded by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showErrorDetails by remember { mutableStateOf(false) }

    // Animation states
    val density = LocalDensity.current
    var isFormVisible by remember { mutableStateOf(false) }

    // Scroll state for the entire content
    val scrollState = rememberScrollState()

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = false

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = DarkAppColors.BackgroundPrimary,
            darkIcons = useDarkIcons
        )
    }

    // Launch animations
    LaunchedEffect(Unit) {
        delay(100)
        isFormVisible = true
    }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading && uiState.errorMessage == null && glucoseLevel.isNotEmpty()) {
            showSuccessDialog = true
        }
    }

    // Show error dialog when there's an error
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            showErrorDialog = true
            showErrorDetails = false // Reset details view
        }
    }

    // Get validation state
    val isGlucoseValid = glucoseLevel.isEmpty() ||
            (glucoseLevel.toDoubleOrNull()?.let { it > 0 && it <= 999 } == true)
    val canSubmit = glucoseLevel.isNotEmpty() &&
            glucoseLevel.toDoubleOrNull()?.let { it > 0 && it <= 999 } == true

    // Success Dialog
    if (showSuccessDialog) {
        Dialog(onDismissRequest = { }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DarkAppColors.BackgroundCard
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                DarkAppColors.SuccessGreen.copy(alpha = 0.2f),
                                RoundedCornerShape(32.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = DarkAppColors.SuccessGreen,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Reading Saved!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkAppColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your blood sugar reading has been successfully recorded.",
                        fontSize = 14.sp,
                        color = DarkAppColors.TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            onNavigateBack(true)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkAppColors.SuccessGreen
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            "Continue",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }

    // Error Dialog with improved UX
    if (showErrorDialog && uiState.errorMessage != null) {
        Dialog(onDismissRequest = { showErrorDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DarkAppColors.BackgroundCard
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                DarkAppColors.ErrorRed.copy(alpha = 0.2f),
                                RoundedCornerShape(32.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = DarkAppColors.ErrorRed,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Server Error!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkAppColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (showErrorDetails) {
                            uiState.errorMessage ?: "An unexpected error occurred"
                        } else {
                            "Something went wrong while saving your reading. Please try again."
                        },
                        fontSize = 14.sp,
                        color = DarkAppColors.TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.heightIn(max = if (showErrorDetails) 200.dp else 60.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    if (showErrorDetails) {
                        // Single OK button when showing details
                        Button(
                            onClick = {
                                showErrorDialog = false
                                showErrorDetails = false
                                viewModel.clearError()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkAppColors.ErrorRed
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                "OK",
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        // Two buttons when showing simple error
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    showErrorDetails = true
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = DarkAppColors.TextSecondary
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = DarkAppColors.TextHint
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    "Details",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                            }
                            Button(
                                onClick = {
                                    showErrorDialog = false
                                    viewModel.clearError()
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DarkAppColors.ErrorRed
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    "OK",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        containerColor = DarkAppColors.BackgroundPrimary,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Blood Sugar Reading",
                        color = DarkAppColors.TextOnSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigateBack(false) },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = DarkAppColors.TextSecondary
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkAppColors.BackgroundCard,
                    titleContentColor = DarkAppColors.TextOnSurface,
                    navigationIconContentColor = DarkAppColors.TextSecondary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            DarkAppColors.BackgroundPrimary,
                            DarkAppColors.BackgroundSecondary
                        )
                    )
                )
        ) {
            AnimatedVisibility(
                visible = isFormVisible,
                enter = slideInVertically(
                    initialOffsetY = { with(density) { 40.dp.roundToPx() } },
                    animationSpec = tween(600, easing = EaseOutCubic)
                ) + fadeIn(animationSpec = tween(600))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = DarkAppColors.BackgroundCard
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        DarkAppColors.ErrorRed.copy(alpha = 0.2f),
                                        RoundedCornerShape(28.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = DarkAppColors.ErrorRed,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Track Your Health",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkAppColors.TextPrimary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Enter your current blood sugar reading",
                                fontSize = 14.sp,
                                color = DarkAppColors.TextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Glucose Level Input
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = DarkAppColors.BackgroundCard
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = DarkAppColors.PrimaryBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Blood Sugar Level",
                                    fontWeight = FontWeight.SemiBold,
                                    color = DarkAppColors.TextPrimary,
                                    fontSize = 16.sp
                                )
                            }

                            OutlinedTextField(
                                value = glucoseLevel,
                                onValueChange = {
                                    if (it.isEmpty() || it.matches(Regex("^\\d{1,3}(\\.\\d{0,1})?$"))) {
                                        glucoseLevel = it
                                    }
                                },
                                label = {
                                    Text(
                                        "Enter reading (mg/dL)",
                                        color = DarkAppColors.TextHint
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.fillMaxWidth(),
                                isError = !isGlucoseValid,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = DarkAppColors.PrimaryBlue,
                                    unfocusedBorderColor = DarkAppColors.TextHint.copy(alpha = 0.3f),
                                    focusedLabelColor = DarkAppColors.PrimaryBlue,
                                    unfocusedLabelColor = DarkAppColors.TextHint,
                                    focusedTextColor = DarkAppColors.TextPrimary,
                                    unfocusedTextColor = DarkAppColors.TextPrimary,
                                    errorBorderColor = DarkAppColors.ErrorRed,
                                    errorLabelColor = DarkAppColors.ErrorRed,
                                    cursorColor = DarkAppColors.PrimaryBlue,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent
                                ),
                                supportingText = if (!isGlucoseValid && glucoseLevel.isNotEmpty()) {
                                    {
                                        Text(
                                            "Please enter a valid reading (1-999 mg/dL)",
                                            color = DarkAppColors.ErrorRed,
                                            fontSize = 12.sp
                                        )
                                    }
                                } else {
                                    {
                                        Text(
                                            "Normal range: 70-180 mg/dL",
                                            color = DarkAppColors.TextHint,
                                            fontSize = 12.sp
                                        )
                                    }
                                },
                                trailingIcon = {
                                    if (glucoseLevel.isNotEmpty()) {
                                        val level = glucoseLevel.toDoubleOrNull()
                                        level?.let {
                                            Icon(
                                                imageVector = when {
                                                    it < 70 -> Icons.Default.TrendingDown
                                                    it > 180 -> Icons.Default.TrendingUp
                                                    else -> Icons.Default.CheckCircle
                                                },
                                                contentDescription = null,
                                                tint = when {
                                                    it < 70 -> DarkAppColors.SugarLow
                                                    it > 180 -> DarkAppColors.SugarHigh
                                                    else -> DarkAppColors.SugarNormal
                                                }
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }

                    // Meal Context Dropdown
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = DarkAppColors.BackgroundCard
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Restaurant,
                                    contentDescription = null,
                                    tint = DarkAppColors.SecondaryTeal,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Meal Context",
                                    fontWeight = FontWeight.SemiBold,
                                    color = DarkAppColors.TextPrimary,
                                    fontSize = 16.sp
                                )
                            }

                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedMealContext.name.replace("_", " ").uppercase(),
                                    onValueChange = {},
                                    readOnly = true,
                                    label = {
                                        Text(
                                            "Select timing",
                                            color = DarkAppColors.TextHint
                                        )
                                    },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expanded,
                                            modifier = Modifier.alpha(0.7f)
                                        )
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = DarkAppColors.SecondaryTeal,
                                        unfocusedBorderColor = DarkAppColors.TextHint.copy(alpha = 0.3f),
                                        focusedLabelColor = DarkAppColors.SecondaryTeal,
                                        unfocusedLabelColor = DarkAppColors.TextHint,
                                        focusedTextColor = DarkAppColors.TextPrimary,
                                        unfocusedTextColor = DarkAppColors.TextPrimary,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent
                                    )
                                )

                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.background(DarkAppColors.BackgroundCard)
                                ) {
                                    MealContext.values().forEach { context ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = if (context == MealContext.BEFORE_MEAL)
                                                            Icons.Default.WatchLater else Icons.Default.Schedule,
                                                        contentDescription = null,
                                                        tint = if (context == MealContext.BEFORE_MEAL)
                                                            DarkAppColors.BeforeMealText else DarkAppColors.AfterMealText,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                    Text(
                                                        context.name.replace("_", " ").uppercase(),
                                                        color = DarkAppColors.TextPrimary
                                                    )
                                                }
                                            },
                                            onClick = {
                                                selectedMealContext = context
                                                expanded = false
                                            },
                                            colors = MenuDefaults.itemColors(
                                                textColor = DarkAppColors.TextPrimary
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Notes Input
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = DarkAppColors.BackgroundCard
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notes,
                                    contentDescription = null,
                                    tint = DarkAppColors.WarningOrange,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Additional Notes",
                                    fontWeight = FontWeight.SemiBold,
                                    color = DarkAppColors.TextPrimary,
                                    fontSize = 16.sp
                                )
                            }

                            OutlinedTextField(
                                value = notes,
                                onValueChange = { notes = it },
                                label = {
                                    Text(
                                        "Optional notes...",
                                        color = DarkAppColors.TextHint
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 3,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = DarkAppColors.WarningOrange,
                                    unfocusedBorderColor = DarkAppColors.TextHint.copy(alpha = 0.3f),
                                    focusedLabelColor = DarkAppColors.WarningOrange,
                                    unfocusedLabelColor = DarkAppColors.TextHint,
                                    focusedTextColor = DarkAppColors.TextPrimary,
                                    unfocusedTextColor = DarkAppColors.TextPrimary,
                                    cursorColor = DarkAppColors.WarningOrange,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent
                                )
                            )
                        }
                    }

                    // Submit Button
                    AnimatedContent(
                        targetState = uiState.isLoading,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) with
                                    fadeOut(animationSpec = tween(300))
                        }
                    ) { isLoading ->
                        Button(
                            onClick = {
                                val glucose = glucoseLevel.toDoubleOrNull()
                                if (glucose != null && glucose > 0) {
                                    viewModel.addReading(glucose,
                                        notes.ifBlank { null }.toString(), selectedMealContext)
                                }
                            },
                            enabled = !isLoading && canSubmit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkAppColors.SecondaryTeal,
                                contentColor = DarkAppColors.TextOnPrimary,
                                disabledContainerColor = DarkAppColors.TextHint.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(20.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                        ) {
                            if (isLoading) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = DarkAppColors.TextOnPrimary,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        "Saving...",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Save Reading",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // Extra bottom padding
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}