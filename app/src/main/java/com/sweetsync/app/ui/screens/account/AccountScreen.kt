package com.sweetsync.app.ui.screens.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sweetsync.app.R
import com.sweetsync.app.ui.components.MemberAvatarView
import com.sweetsync.app.ui.components.SweetSyncDetailTopBar
import com.sweetsync.app.ui.theme.AppPrimary
import com.sweetsync.app.ui.theme.AppPrimaryTint
import com.sweetsync.app.ui.theme.BpCrimson
import com.sweetsync.app.ui.theme.CardShape
import com.sweetsync.app.ui.theme.CardStroke
import com.sweetsync.app.ui.theme.CardSurface
import com.sweetsync.app.ui.theme.InnerCardShape
import com.sweetsync.app.ui.theme.PillShape
import com.sweetsync.app.ui.theme.ScreenBackground
import com.sweetsync.app.ui.theme.StatusOptimalBg
import com.sweetsync.app.ui.theme.StatusOptimalText
import com.sweetsync.app.ui.theme.TextLight
import com.sweetsync.app.ui.theme.TextPrimary
import com.sweetsync.app.ui.theme.TextSecondary

@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = uiState.userProfile

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        SweetSyncDetailTopBar(
            title = "Account & Sync",
            onBackClick = onNavigateBack,
            onProfileClick = { }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, CardShape)
                        .clip(CardShape)
                        .background(CardSurface)
                        .border(1.dp, CardStroke, CardShape)
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_sarah),
                            contentDescription = "Profile Photo",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .border(2.dp, AppPrimary, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user.displayName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .background(AppPrimaryTint, PillShape)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (user.isGuest) "Guest Account" else "Verified Family",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = AppPrimary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = user.email.ifBlank { "Unlinked guest session" },
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${uiState.membersCount} Family Members • ${uiState.recordsCount} Active Records",
                                fontSize = 11.sp,
                                color = TextLight
                            )
                        }
                    }
                }
            }

            // Cloud Sync Status Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, CardShape)
                        .clip(CardShape)
                        .background(CardSurface)
                        .border(1.dp, CardStroke, CardShape)
                        .padding(18.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(AppPrimaryTint),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudDone,
                                        contentDescription = null,
                                        tint = AppPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Google & Firebase Sync",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Cloud Firestore Real-Time DB",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFD1FAE5), PillShape)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Connected",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusOptimalText
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Automatic cloud backup keeps telemetry and logs synchronized across your family members' devices in real time.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { viewModel.toggleGuestGoogleLink() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (user.isGuest) Color(0xFF1A73E8) else Color(0xFFF1F5F9)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Cloud,
                                contentDescription = null,
                                tint = if (user.isGuest) Color.White else TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (user.isGuest) "Link Google Account to Sync" else "Google Account Linked (Tap to switch)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (user.isGuest) Color.White else TextPrimary
                            )
                        }
                    }
                }
            }

            // Health Tracks Management
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, CardShape)
                        .clip(CardShape)
                        .background(CardSurface)
                        .border(1.dp, CardStroke, CardShape)
                        .padding(18.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Preferences & Export",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Export Data
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.FileDownload, contentDescription = null, tint = AppPrimary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = "Export Health History (PDF / CSV)", fontSize = 13.sp, color = TextPrimary)
                            }
                            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        }

                        // Privacy & Encryption
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = AppPrimary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = "End-to-End Medical Privacy", fontSize = 13.sp, color = TextPrimary)
                            }
                            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        }

                        // Sign Out
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.signOut() }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.AutoMirrored.Outlined.Logout, contentDescription = null, tint = BpCrimson, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = "Reset / Sign Out Session", fontSize = 13.sp, color = BpCrimson, fontWeight = FontWeight.SemiBold)
                            }
                            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            // App Version Info
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sweet Sync v1.0.0",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                    Text(
                        text = "Clean Architecture • Jetpack Compose • Firebase",
                        fontSize = 11.sp,
                        color = TextLight
                    )
                }
            }
        }
    }
}
