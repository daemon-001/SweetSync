package com.sweetsync.app.ui.screens.memberdetails

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sweetsync.app.data.model.FamilyMember
import com.sweetsync.app.data.model.TrackType
import com.sweetsync.app.ui.components.MemberAvatarView
import com.sweetsync.app.ui.components.MultiTrackCalendar
import com.sweetsync.app.ui.components.QuickLogVitalSheet
import com.sweetsync.app.ui.components.StatusBadge
import com.sweetsync.app.ui.components.SweetSyncDetailTopBar
import com.sweetsync.app.ui.theme.AppPrimary
import com.sweetsync.app.ui.theme.AppPrimaryTint
import com.sweetsync.app.ui.theme.BpBg
import com.sweetsync.app.ui.theme.BpCrimson
import com.sweetsync.app.ui.theme.CardShape
import com.sweetsync.app.ui.theme.CardStroke
import com.sweetsync.app.ui.theme.CardSurface
import com.sweetsync.app.ui.theme.InnerCardShape
import com.sweetsync.app.ui.theme.PillShape
import com.sweetsync.app.ui.theme.ScreenBackground
import com.sweetsync.app.ui.theme.StatusOptimalBg
import com.sweetsync.app.ui.theme.StatusOptimalText
import com.sweetsync.app.ui.theme.SugarBg
import com.sweetsync.app.ui.theme.SugarDot
import com.sweetsync.app.ui.theme.SugarOrange
import com.sweetsync.app.ui.theme.TextLight
import com.sweetsync.app.ui.theme.TextPrimary
import com.sweetsync.app.ui.theme.TextSecondary
import com.sweetsync.app.ui.theme.WeightTeal

@Composable
fun MemberDetailScreen(
    viewModel: MemberDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToTrack: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val member = uiState.member

    if (uiState.showQuickLogSheet && member != null) {
        QuickLogVitalSheet(
            familyMembers = listOf(member),
            initialMemberId = member.id,
            onDismiss = { viewModel.closeQuickLog() },
            onSaveRecord = { record -> viewModel.saveQuickLogRecord(record) }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        SweetSyncDetailTopBar(
            title = "Member Details",
            onBackClick = onNavigateBack,
            onProfileClick = onNavigateToProfile
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Profile Summary Card
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
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MemberAvatarView(
                                drawableName = member?.avatarDrawableName ?: "avatar_robert",
                                initials = member?.initials ?: "RM",
                                size = 64.dp,
                                showVerifiedBadge = true,
                                badgeColor = AppPrimary
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = member?.fullName ?: "Robert Miller",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    StatusBadge(
                                        text = member?.relationshipDisplay ?: "Father • 62y",
                                        textColor = Color(0xFF475569),
                                        backgroundColor = Color(0xFFF1F5F9)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.AccessTime,
                                        contentDescription = null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Logged today at 8:15 AM",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Device Sync Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(InnerCardShape)
                                .background(Color(0xFFF0FDF4).copy(alpha = 0.7f))
                                .border(1.dp, Color(0xFFCCFBF1), InnerCardShape)
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Favorite,
                                        contentDescription = null,
                                        tint = AppPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Synchronized with ${member?.deviceSyncName ?: "Omron i-C10"}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextPrimary
                                    )
                                }

                                Button(
                                    onClick = { viewModel.openQuickLog() },
                                    colors = ButtonDefaults.buttonColors(containerColor = AppPrimary),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Log Vital",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Active Health Tracks Section
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ShowChart,
                                contentDescription = null,
                                tint = AppPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Active Health Tracks",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE0F2FE), PillShape)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            val activeCount = member?.activeTracks?.size ?: 2
                            Text(
                                text = "$activeCount Assigned",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0284C7)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Track 1: Blood Sugar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(CardSurface)
                            .border(1.dp, CardStroke, CardShape)
                            .clickable { onNavigateToTrack("blood_sugar") }
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFFEDD5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Opacity,
                                        contentDescription = null,
                                        tint = SugarOrange,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Blood Sugar",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(SugarDot, CircleShape)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Active • 2 daily targets",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEFF6FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = Color(0xFF1E3A8A),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Track 2: Blood Pressure
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(CardSurface)
                            .border(1.dp, CardStroke, CardShape)
                            .clickable { onNavigateToTrack("blood_pressure") }
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFFFE4E6)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Favorite,
                                        contentDescription = null,
                                        tint = BpCrimson,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Blood Pressure",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(BpCrimson, CircleShape)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Active • SYS / DIA / HR",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEFF6FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = Color(0xFF1E3A8A),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Track 3: Unassigned Available Track: Weight
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, CardStroke, CardShape)
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFE0F2FE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Scale,
                                        contentDescription = null,
                                        tint = WeightTeal,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Available Track: Body\nComposition & Weight",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        lineHeight = 17.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Tailored for senior mobility &\nhydration",
                                        fontSize = 11.sp,
                                        color = TextSecondary,
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(Color(0xFFE0F2FE))
                                    .clickable { viewModel.assignWeightTrack() }
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = AppPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Assign",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AppPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Interactive Multi-Track Calendar UI
            item {
                MultiTrackCalendar(
                    selectedDate = uiState.selectedDate,
                    onDateSelected = { viewModel.onDateSelected(it) },
                    trackRecordsMap = uiState.monthlyTrackMap
                )
            }

            // 4. Logs for Selected Day Section
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Logs for Oct 24, Today",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFD1FAE5), PillShape)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Verified",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusOptimalText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "2 entries recorded for Robert",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Reading 1: Fasting Glucose
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(CardSurface)
                            .border(1.dp, CardStroke, CardShape)
                            .padding(14.dp)
                    ) {
                        Column {
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
                                            .background(Color(0xFFFFEDD5)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Opacity,
                                            contentDescription = null,
                                            tint = SugarOrange,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Fasting Glucose",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "7:45 AM",
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
                                        text = "✓ Optimal",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = StatusOptimalText
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "104",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = " mg/dL",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(bottom = 3.dp)
                                    )
                                }

                                Text(
                                    text = "Target: 70-110",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Reading 2: Morning Vitals
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(CardSurface)
                            .border(1.dp, CardStroke, CardShape)
                            .padding(14.dp)
                    ) {
                        Column {
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
                                            .background(Color(0xFFFFE4E6)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Favorite,
                                            contentDescription = null,
                                            tint = BpCrimson,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Morning Vitals",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "8:15 AM",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                StatusBadge(text = "Normal")
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "124/82",
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = " mmHg",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                }

                                Text(
                                    text = "AHA Standard",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "♥", fontSize = 12.sp, color = BpCrimson)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Pulse: 72 bpm",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Reading 3: Evening Reminder Active
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(Color(0xFFE8F0FE).copy(alpha = 0.6f))
                            .border(1.dp, Color(0xFFBFDBFE), CardShape)
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(AppPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Evening Reminder Active",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Next: Post-dinner glucose check at 7:30 PM",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
