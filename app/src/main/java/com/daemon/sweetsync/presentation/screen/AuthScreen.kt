package com.daemon.sweetsync.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.daemon.sweetsync.presentation.viewmodel.AuthViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// Define colors matching the home screen theme
object AuthScreenColors {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showFullError by remember { mutableStateOf(false) }
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = AuthScreenColors.BackgroundPrimary,
            darkIcons = false
        )
    }

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onAuthSuccess()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.checkAuthStatus()
    }

    // Show error dialog when there's an error message
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            showErrorDialog = true
        }
    }

    // Error Dialog
    if (showErrorDialog && uiState.errorMessage != null) {
        AuthErrorDialog(
            errorMessage = uiState.errorMessage!!,
            showFullError = showFullError,
            onShowFullError = { showFullError = true },
            onDismiss = {
                showErrorDialog = false
                showFullError = false
                viewModel.clearError()
            },
            onRetry = {
                showErrorDialog = false
                showFullError = false
                viewModel.clearError()
                // Retry the last action
                if (isSignUp) {
                    viewModel.signUp(email, password, name)
                } else {
                    viewModel.signIn(email, password)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthScreenColors.BackgroundPrimary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Show verification success message
            if (uiState.verificationEmailSent && uiState.isSignUpSuccess) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    AuthScreenColors.TealPrimary.copy(alpha = 0.1f),
                                    AuthScreenColors.TealSecondary.copy(alpha = 0.05f)
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = AuthScreenColors.SuccessGreen,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Account Created Successfully!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AuthScreenColors.TextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = AuthScreenColors.TealPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Verification email sent to $email",
                                fontSize = 14.sp,
                                color = AuthScreenColors.TextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Please check your email and click the verification link to activate your account.",
                            fontSize = 13.sp,
                            color = AuthScreenColors.TextSecondary,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                isSignUp = false
                                email = ""
                                password = ""
                                name = ""
                                viewModel.resetToSignIn()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AuthScreenColors.TealPrimary,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                "Continue to Sign In",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            } else {
                // App branding section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // App icon placeholder (you can replace with actual icon)
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(AuthScreenColors.TealPrimary, AuthScreenColors.TealSecondary)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🩸",
                            fontSize = 32.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "SweetSync",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = AuthScreenColors.TextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Blood Sugar Tracker",
                        fontSize = 16.sp,
                        color = AuthScreenColors.TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Auth form
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Name field (only shown during sign up)
                    if (isSignUp) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = {
                                Text(
                                    "Full Name",
                                    color = AuthScreenColors.TextSecondary
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AuthScreenColors.TealPrimary,
                                unfocusedBorderColor = AuthScreenColors.TextSecondary.copy(alpha = 0.5f),
                                focusedTextColor = AuthScreenColors.TextPrimary,
                                unfocusedTextColor = AuthScreenColors.TextPrimary,
                                cursorColor = AuthScreenColors.TealPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = {
                            Text(
                                "Email",
                                color = AuthScreenColors.TextSecondary
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AuthScreenColors.TealPrimary,
                            unfocusedBorderColor = AuthScreenColors.TextSecondary.copy(alpha = 0.5f),
                            focusedTextColor = AuthScreenColors.TextPrimary,
                            unfocusedTextColor = AuthScreenColors.TextPrimary,
                            cursorColor = AuthScreenColors.TealPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = {
                            Text(
                                "Password",
                                color = AuthScreenColors.TextSecondary
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AuthScreenColors.TealPrimary,
                            unfocusedBorderColor = AuthScreenColors.TextSecondary.copy(alpha = 0.5f),
                            focusedTextColor = AuthScreenColors.TextPrimary,
                            unfocusedTextColor = AuthScreenColors.TextPrimary,
                            cursorColor = AuthScreenColors.TealPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            if (isSignUp) {
                                viewModel.signUp(email, password, name)
                            } else {
                                viewModel.signIn(email, password)
                            }
                        },
                        enabled = !uiState.isLoading &&
                                email.isNotBlank() &&
                                password.isNotBlank() &&
                                (!isSignUp || name.isNotBlank()),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AuthScreenColors.TealPrimary,
                            contentColor = Color.Black,
                            disabledContainerColor = AuthScreenColors.TealPrimary.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.Black,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                if (isSignUp) "Sign Up" else "Sign In",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(
                        onClick = {
                            isSignUp = !isSignUp
                            // Clear fields when switching modes
                            name = ""
                            email = ""
                            password = ""
                            viewModel.clearError()
                            viewModel.clearVerificationMessage()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (isSignUp) "Already have an account? Sign In"
                            else "Don't have an account? Sign Up",
                            color = AuthScreenColors.TealPrimary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthErrorDialog(
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
                containerColor = AuthScreenColors.CardBackground
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = AuthScreenColors.ErrorRed.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = AuthScreenColors.ErrorRed,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Server Error !",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuthScreenColors.TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (showFullError) {
                        errorMessage
                    } else {
                        "Unable to connect to server. Please check your internet connection and try again."
                    },
                    fontSize = 14.sp,
                    color = AuthScreenColors.TextSecondary,
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
                            colors = CardDefaults.cardColors(
                                containerColor = AuthScreenColors.BackgroundSecondary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Details",
                                    fontWeight = FontWeight.Medium,
                                    color = AuthScreenColors.TextSecondary,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .let { if (!showFullError) it.weight(1f) else it.fillMaxWidth() }
                            .clickable { onRetry() },
                        colors = CardDefaults.cardColors(
                            containerColor = AuthScreenColors.TealPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Retry",
                                color = Color.Black,
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