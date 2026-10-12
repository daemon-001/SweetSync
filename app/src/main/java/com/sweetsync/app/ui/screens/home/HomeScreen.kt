package com.sweetsync.app.ui.screens.home

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.sweetsync.app.ui.components.EcgPulseWave
import com.sweetsync.app.ui.components.MemberAvatarView
import com.sweetsync.app.ui.components.MiniSugarWave
import com.sweetsync.app.ui.components.QuickLogVitalSheet
import com.sweetsync.app.ui.components.StatusBadge
import com.sweetsync.app.ui.components.SweetSyncHomeTopBar
import com.sweetsync.app.ui.components.WeightTrendBars
import com.sweetsync.app.ui.theme.AppPrimary
import com.sweetsync.app.ui.theme.BpBg
import com.sweetsync.app.ui.theme.BpCrimson
import com.sweetsync.app.ui.theme.BpDot
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
import com.sweetsync.app.ui.theme.WeightBg
import com.sweetsync.app.ui.theme.WeightDot
import com.sweetsync.app.ui.theme.WeightTeal

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToMemberDetail: (String) -> Unit,
    onNavigateToTrack: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.showQuickLogSheet) {
        QuickLogVitalSheet(
            familyMembers = uiState.familyMembers,
            initialMemberId = uiState.quickLogMemberId,
            onDismiss = { viewModel.closeQuickLog() },
            onSaveRecord = { record -> viewModel.saveQuickLogRecord(record) }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        SweetSyncHomeTopBar(
            onNotificationsClick = { },
            onProfileClick = onNavigateToProfile
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. Greeting & Sync Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Hello, ${uiState.currentUser.displayName.split(" ").first()} 👋",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(StatusOptimalText, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "All ${uiState.familyMembers.size} family members in sync",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    // Google Sync pill button
                    Box(
                        modifier = Modifier
                            .clip(PillShape)
                            .background(Color(0xFFE8F0FE))
                            .clickable { viewModel.toggleSync() }
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Cloud,
                                contentDescription = null,
                                tint = Color(0xFF1A73E8),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Google Sync",
                                color = Color(0xFF1A73E8),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // 2. Primary Action: Quick Log Vital + Filter
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { viewModel.openQuickLog() },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .shadow(2.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Quick Log Vital",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CardSurface)
                            .border(1.dp, CardStroke, RoundedCornerShape(16.dp))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Tune,
                            contentDescription = "Filter",
                            tint = TextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // 3. Timeline Sync Strip
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Timeline Sync",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "October 2024",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AppPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 7-day selector strip
                    data class DayItem(
                        val dayOfWeek: String,
                        val dayNum: Int,
                        val dateString: String,
                        val dots: List<Color>
                    )

                    val timelineDays = listOf(
                        DayItem("Mon", 21, "2024-10-21", listOf(SugarDot, BpDot)),
                        DayItem("Tue", 22, "2024-10-22", listOf(SugarDot, WeightDot)),
                        DayItem("Wed", 23, "2024-10-23", listOf(BpDot)),
                        DayItem("Thu", 24, "2024-10-24", listOf(SugarDot, BpDot, WeightDot)),
                        DayItem("Fri", 25, "2024-10-25", emptyList()),
                        DayItem("Sat", 26, "2024-10-26", emptyList()),
                        DayItem("Sun", 27, "2024-10-27", emptyList())
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        timelineDays.forEach { item ->
                            val isSelected = item.dateString == uiState.selectedDate

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(68.dp)
                                    .padding(horizontal = 2.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (isSelected) AppPrimary else CardSurface
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) AppPrimary else CardStroke,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable { viewModel.onDateSelected(item.dateString) },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = item.dayOfWeek,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isSelected) Color.White.copy(alpha = 0.85f) else TextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = item.dayNum.toString(),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    if (item.dots.isNotEmpty()) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            item.dots.forEach { dotColor ->
                                                Box(
                                                    modifier = Modifier
                                                        .size(4.dp)
                                                        .background(dotColor, CircleShape)
                                                )
                                            }
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. Active Tracks Across Family
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Active Tracks",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Across Family",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Blood Sugar Card
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(InnerCardShape)
                                .background(SugarBg)
                                .border(1.dp, Color(0xFFFFEDD5), InnerCardShape)
                                .clickable { onNavigateToTrack("blood_sugar") }
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Opacity,
                                        contentDescription = null,
                                        tint = SugarOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFFFEDD5), PillShape)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(text = "2 Logs", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SugarOrange)
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Blood Sugar",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                MiniSugarWave()
                            }
                        }

                        // Vascular BP Card
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(InnerCardShape)
                                .background(BpBg)
                                .border(1.dp, Color(0xFFFFE4E6), InnerCardShape)
                                .clickable { onNavigateToTrack("blood_pressure") }
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Favorite,
                                        contentDescription = null,
                                        tint = BpCrimson,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFFFE4E6), PillShape)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(text = "1 Log", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Vascular BP",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                EcgPulseWave(height = 18.dp)
                            }
                        }

                        // Body Weight Card
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(InnerCardShape)
                                .background(WeightBg)
                                .border(1.dp, Color(0xFFCCFBF1), InnerCardShape)
                                .clickable { onNavigateToTrack("weight") }
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Scale,
                                        contentDescription = null,
                                        tint = WeightTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFCCFBF1), PillShape)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(text = "3 Logs", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WeightTeal)
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Body Weight",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                WeightTrendBars()
                            }
                        }
                    }
                }
            }

            // 5. Family Vitals Feed Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Family Vitals Feed",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Real-time status",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            // Card 1: Robert Miller
            item {
                val robert = uiState.familyMembers.find { it.fullName.contains("Robert") }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, CardShape)
                        .clip(CardShape)
                        .background(CardSurface)
                        .border(1.dp, CardStroke, CardShape)
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                MemberAvatarView(
                                    drawableName = "avatar_robert",
                                    initials = "RM",
                                    size = 46.dp,
                                    ringColor = Color(0xFF10B981)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Robert Miller",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        StatusBadge(
                                            text = "Father • 62y",
                                            textColor = Color(0xFF475569),
                                            backgroundColor = Color(0xFFF1F5F9)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(5.dp).background(Color(0xFF005B52), CircleShape))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Logged 8:15 AM Today",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0F2FE))
                                    .clickable { viewModel.openQuickLog(robert?.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Quick Add",
                                    tint = AppPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Subcard 1: Blood Glucose
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(InnerCardShape)
                                .background(Color(0xFFF8FAFC))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Outlined.Opacity,
                                            contentDescription = null,
                                            tint = SugarOrange,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Blood Glucose",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                    }
                                    StatusBadge(text = "In Target")
                                }

                                Spacer(modifier = Modifier.height(6.dp))

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
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Fasting (7:45 AM)",
                                        fontSize = 12.sp,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(bottom = 3.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Post-Lunch: 138 mg/dL",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "Yesterday",
                                        fontSize = 12.sp,
                                        color = TextLight
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Subcard 2: Blood Pressure
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(InnerCardShape)
                                .background(Color(0xFFF8FAFC))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Outlined.Favorite,
                                            contentDescription = null,
                                            tint = BpCrimson,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Blood Pressure",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                    }
                                    StatusBadge(text = "Optimal")
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            text = "124 / 82",
                                            fontSize = 24.sp,
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

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "♥",
                                            fontSize = 13.sp,
                                            color = BpCrimson
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "72 bpm",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = BpCrimson
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Footer
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    robert?.let { onNavigateToMemberDetail(it.id) }
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "View Detailed History & Calendar",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = AppPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "2 Active Tracks",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Card 2: Elena Miller
            item {
                val elena = uiState.familyMembers.find { it.fullName.contains("Elena") }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, CardShape)
                        .clip(CardShape)
                        .background(CardSurface)
                        .border(1.dp, CardStroke, CardShape)
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                MemberAvatarView(
                                    drawableName = "avatar_elena",
                                    initials = "EM",
                                    size = 46.dp,
                                    ringColor = SugarOrange
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Elena Miller",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        StatusBadge(
                                            text = "Mother • 58y",
                                            textColor = Color(0xFF475569),
                                            backgroundColor = Color(0xFFF1F5F9)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(5.dp).background(Color(0xFF005B52), CircleShape))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Logged 8:00 AM Today",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0F2FE))
                                    .clickable { viewModel.openQuickLog(elena?.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Quick Add",
                                    tint = AppPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Dual Card Row: Sugar | Weight
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Sugar
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(InnerCardShape)
                                    .background(Color(0xFFF8FAFC))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Outlined.Opacity,
                                                contentDescription = null,
                                                tint = SugarOrange,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "Sugar", fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
                                        }
                                        StatusBadge(text = "Fasting")
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "96", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = " mg/dL", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = "8:00 AM Today", fontSize = 11.sp, color = TextLight)
                                }
                            }

                            // Weight
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(InnerCardShape)
                                    .background(Color(0xFFF8FAFC))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Outlined.Scale,
                                                contentDescription = null,
                                                tint = WeightTeal,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "Weight", fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
                                        }
                                        StatusBadge(text = "-0.4 kg", textColor = StatusOptimalText, backgroundColor = StatusOptimalBg)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "64.2", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = " kg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = "Fasting (Morning)", fontSize = 11.sp, color = TextLight)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Subcard BP
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(InnerCardShape)
                                .background(Color(0xFFF8FAFC))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.Favorite,
                                        contentDescription = null,
                                        tint = BpCrimson,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "118 / 78 mmHg",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "Resting BP • Normal",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "♥", fontSize = 12.sp, color = BpCrimson)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "68 bpm", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BpCrimson)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Footer
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    elena?.let { onNavigateToMemberDetail(it.id) }
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "View Detailed History & Calendar",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = AppPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "3 Active Tracks",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Card 3: Lucas Miller
            item {
                val lucas = uiState.familyMembers.find { it.fullName.contains("Lucas") }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, CardShape)
                        .clip(CardShape)
                        .background(CardSurface)
                        .border(1.dp, CardStroke, CardShape)
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                MemberAvatarView(
                                    drawableName = "avatar_lucas",
                                    initials = "LM",
                                    size = 46.dp,
                                    ringColor = Color(0xFF38BDF8)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Lucas Miller",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        StatusBadge(
                                            text = "Son • 19y",
                                            textColor = Color(0xFF475569),
                                            backgroundColor = Color(0xFFF1F5F9)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(5.dp).background(Color(0xFF005B52), CircleShape))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Logged 2:15 PM Today",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0F2FE))
                                    .clickable { viewModel.openQuickLog(lucas?.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Quick Add",
                                    tint = AppPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Subcard: Body Composition Track
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(InnerCardShape)
                                .background(Color(0xFFF8FAFC))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Outlined.Scale,
                                            contentDescription = null,
                                            tint = WeightTeal,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Body Composition Track",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                    }
                                    StatusBadge(text = "Active Athlete")
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.White)
                                            .padding(10.dp)
                                    ) {
                                        Column {
                                            Text(text = "Fasting (Morning)", fontSize = 11.sp, color = TextSecondary)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Row(verticalAlignment = Alignment.Bottom) {
                                                Text(text = "72.8", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                                Text(text = " kg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                            }
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.White)
                                            .padding(10.dp)
                                    ) {
                                        Column {
                                            Text(text = "Post-Lunch (2:15 PM)", fontSize = 11.sp, color = TextSecondary)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Row(verticalAlignment = Alignment.Bottom) {
                                                Text(text = "73.5", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                                Text(text = " kg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Footer
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    lucas?.let { onNavigateToMemberDetail(it.id) }
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "View Detailed History & Calendar",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = AppPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "1 Active Track",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Card 4: Sarah Miller
            item {
                val sarah = uiState.familyMembers.find { it.fullName.contains("Sarah") }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, CardShape)
                        .clip(CardShape)
                        .background(CardSurface)
                        .border(1.dp, CardStroke, CardShape)
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                MemberAvatarView(
                                    drawableName = "avatar_sarah",
                                    initials = "SM",
                                    size = 46.dp,
                                    ringColor = AppPrimary
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Sarah Miller",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        StatusBadge(
                                            text = "Self (Primary)",
                                            textColor = AppPrimary,
                                            backgroundColor = Color(0xFFE6F4F1)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(5.dp).background(Color(0xFF005B52), CircleShape))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "All Daily Targets Complete",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0F2FE))
                                    .clickable { viewModel.openQuickLog(sarah?.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Quick Add",
                                    tint = AppPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Dual Row: Glucose | BP / HR
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Glucose
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(InnerCardShape)
                                    .background(Color(0xFFF8FAFC))
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Outlined.Opacity,
                                            contentDescription = null,
                                            tint = SugarOrange,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column {
                                            Text(text = "Glucose", fontSize = 11.sp, color = TextSecondary)
                                            Row(verticalAlignment = Alignment.Bottom) {
                                                Text(text = "92", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                                Text(text = " mg/dL", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 1.dp))
                                            }
                                        }
                                    }
                                    Box(modifier = Modifier.size(7.dp).background(Color(0xFF005B52), CircleShape))
                                }
                            }

                            // BP / HR
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(InnerCardShape)
                                    .background(Color(0xFFF8FAFC))
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Outlined.Favorite,
                                            contentDescription = null,
                                            tint = BpCrimson,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column {
                                            Text(text = "BP / HR", fontSize = 11.sp, color = TextSecondary)
                                            Text(text = "116/74 • 65", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        }
                                    }
                                    Box(modifier = Modifier.size(7.dp).background(Color(0xFF005B52), CircleShape))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Subcard Weight Baseline
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(InnerCardShape)
                                .background(Color(0xFFF8FAFC))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.Scale,
                                        contentDescription = null,
                                        tint = WeightTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(text = "Weight Baseline", fontSize = 11.sp, color = TextSecondary)
                                        Text(text = "57.4 kg Stable trend", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    }
                                }
                                StatusBadge(text = "Tracked")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Footer
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    sarah?.let { onNavigateToMemberDetail(it.id) }
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "View Detailed History & Calendar",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = AppPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "3 Active Tracks",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Bottom padding
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
