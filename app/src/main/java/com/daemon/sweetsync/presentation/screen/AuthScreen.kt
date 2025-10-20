package com.daemon.sweetsync.presentation.screen

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.daemon.sweetsync.R
import com.daemon.sweetsync.presentation.viewmodel.AuthViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

// Enhanced Dark Theme Color Definitions (matching HomeScreen)
object AuthScreenColors {
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
    val CardBackground = Color(0xFF242424)         // Enhanced card background
    val BackgroundSurface = Color(0xFF1E1E1E)      // Surface background
    val BackgroundElevated = Color(0xFF2A2A2A)     // Elevated card background

    // Dark Theme Text Colors
    val TextPrimary = Color(0xFFF5F5F5)            // Brighter primary text
    val TextSecondary = Color(0xFFB8B8B8)          // Secondary text
    val TextHint = Color(0xFF808080)               // Hint text
    val TextOnPrimary = Color(0xFF000000)          // Text on primary colored backgrounds
    val TextOnSurface = Color(0xFFF0F0F0)          // Text on surface

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
    val context = LocalContext.current
    
    // Google Sign-In launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { idToken ->
                    viewModel.signInWithGoogle(idToken)
                }
            } catch (e: ApiException) {
                Log.e("AuthScreen", "Google sign-in failed", e)
                // Handle error silently or show error to user
            }
        }
    }

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
                                    AuthScreenColors.SecondaryTeal.copy(alpha = 0.15f),
                                    AuthScreenColors.PrimaryBlue.copy(alpha = 0.05f)
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
                                tint = AuthScreenColors.SecondaryTeal,
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
                                containerColor = AuthScreenColors.SecondaryTeal,
                                contentColor = Color.White
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
                                    colors = listOf(AuthScreenColors.SecondaryTeal, AuthScreenColors.PrimaryBlue)
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
                                focusedBorderColor = AuthScreenColors.SecondaryTeal,
                                unfocusedBorderColor = AuthScreenColors.TextSecondary.copy(alpha = 0.3f),
                                focusedTextColor = AuthScreenColors.TextPrimary,
                                unfocusedTextColor = AuthScreenColors.TextPrimary,
                                cursorColor = AuthScreenColors.SecondaryTeal,
                                focusedLabelColor = AuthScreenColors.SecondaryTeal,
                                unfocusedLabelColor = AuthScreenColors.TextSecondary
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
                            focusedBorderColor = AuthScreenColors.SecondaryTeal,
                            unfocusedBorderColor = AuthScreenColors.TextSecondary.copy(alpha = 0.3f),
                            focusedTextColor = AuthScreenColors.TextPrimary,
                            unfocusedTextColor = AuthScreenColors.TextPrimary,
                            cursorColor = AuthScreenColors.SecondaryTeal,
                            focusedLabelColor = AuthScreenColors.SecondaryTeal,
                            unfocusedLabelColor = AuthScreenColors.TextSecondary
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
                            focusedBorderColor = AuthScreenColors.SecondaryTeal,
                            unfocusedBorderColor = AuthScreenColors.TextSecondary.copy(alpha = 0.3f),
                            focusedTextColor = AuthScreenColors.TextPrimary,
                            unfocusedTextColor = AuthScreenColors.TextPrimary,
                            cursorColor = AuthScreenColors.SecondaryTeal,
                            focusedLabelColor = AuthScreenColors.SecondaryTeal,
                            unfocusedLabelColor = AuthScreenColors.TextSecondary
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
                            containerColor = AuthScreenColors.SecondaryTeal,
                            contentColor = Color.White,
                            disabledContainerColor = AuthScreenColors.SecondaryTeal.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
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

                    // OR Divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = AuthScreenColors.TextSecondary.copy(alpha = 0.3f)
                        )
                        Text(
                            text = "OR",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = AuthScreenColors.TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = AuthScreenColors.TextSecondary.copy(alpha = 0.3f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Google Sign-In Button
                    OutlinedButton(
                        onClick = {
                            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(context.getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build()
                            val googleSignInClient = GoogleSignIn.getClient(context, gso)
                            googleSignInLauncher.launch(googleSignInClient.signInIntent)
                        },
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black,
                            disabledContainerColor = Color.White.copy(alpha = 0.5f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AuthScreenColors.TextSecondary.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_google_logo),
                                contentDescription = "Google Logo",
                                modifier = Modifier.size(20.dp),
                                tint = androidx.compose.ui.graphics.Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Continue with Google",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
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
                            color = AuthScreenColors.SecondaryTeal,
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
                            containerColor = AuthScreenColors.SecondaryTeal
                        ),
                        shape = RoundedCornerShape(12.dp)
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