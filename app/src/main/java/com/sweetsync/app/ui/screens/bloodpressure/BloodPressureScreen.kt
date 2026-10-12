package com.sweetsync.app.ui.screens.bloodpressure

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
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.filled.AirlineSeatReclineNormal
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.History
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
import com.sweetsync.app.ui.components.EcgPulseWave
import com.sweetsync.app.ui.components.StatusBadge
import com.sweetsync.app.ui.components.SweetSyncDetailTopBar
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
import com.sweetsync.app.ui.theme.TextLight
import com.sweetsync.app.ui.theme.TextPrimary
import com.sweetsync.app.ui.theme.TextSecondary

@Composable
fun BloodPressureScreen(
    viewModel: BloodPressureViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        SweetSyncDetailTopBar(
            title = "Blood Pressure Records",
            onBackClick = onNavigateBack,
            onProfileClick = onNavigateToProfile
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Track Header Banner
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFFE4E6), PillShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(6.dp).background(BpCrimson, CircleShape))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "BLOOD PRESSURE TRACK • MMHG & BPM",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BpCrimson
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "♥", fontSize = 12.sp, color = BpCrimson)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "AHA 2024 Guidelines",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BpCrimson
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Member Filter Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val filters = listOf("All Family", "Robert Miller (Active)", "Elena Miller")
                        filters.forEach { filter ->
                            val isSel = uiState.selectedMemberFilter == filter
                            Box(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(
                                        if (isSel) BpCrimson else Color(0xFFF1F5F9)
                                    )
                                    .clickable { viewModel.onFilterChange(filter) }
                                    .padding(horizontal = 14.dp, vertical = 7.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isSel) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(Color.White, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    Text(
                                        text = filter,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSel) Color.White else TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Latest Vitals Overview Card
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
                                        text = "Latest Vitals Overview",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Robert Miller • Synced via Omron Smart Cuff",
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
                                    text = "Normal / Healthy",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusOptimalText
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Dual Stat Boxes
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Blood Pressure
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(InnerCardShape)
                                    .background(Color(0xFFF8FAFC))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(text = "Blood Pressure", fontSize = 11.sp, color = TextSecondary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "120/80", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                        Text(text = " mmHg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = "↓ -4 mmHg vs last wk", fontSize = 11.sp, color = AppPrimary, fontWeight = FontWeight.Medium)
                                }
                            }

                            // Resting Heart Beat
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(InnerCardShape)
                                    .background(Color(0xFFF8FAFC))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(text = "Resting Heart Beat", fontSize = 11.sp, color = TextSecondary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "71", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                        Text(text = " bpm", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = "♥ Rhythm: Regular", fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 7-Day Systolic Stability Index + ECG Wave
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ShowChart,
                                contentDescription = null,
                                tint = BpCrimson,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "7-Day Systolic Stability Index: 96%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            EcgPulseWave(
                                modifier = Modifier.weight(1f),
                                color = BpCrimson,
                                strokeWidth = 2.dp,
                                height = 20.dp
                            )
                        }
                    }
                }
            }

            // 3. New BP Entry Card
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
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(BpCrimson),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "New BP Entry",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Clinical precision reading mode",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFE4E6), PillShape)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Active Session",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BpCrimson
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Date & Time + Member Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = BpCrimson,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(text = "Date & Time", fontSize = 10.sp, color = TextSecondary)
                                        Text(text = uiState.dateTimeText, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = AppPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(text = "Family Member", fontSize = 10.sp, color = TextSecondary)
                                        Text(text = "Robert Miller", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 3 Core Inputs: SYS | DIA | PULSE
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // SYS Box
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFFFF1F2))
                                    .border(1.dp, Color(0xFFFFE4E6), RoundedCornerShape(14.dp))
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "SYS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = uiState.systolic.toString(),
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BpCrimson
                                    )
                                    Text(text = "mmHg", fontSize = 11.sp, color = TextSecondary)
                                    Text(text = "Systolic", fontSize = 10.sp, color = TextLight)
                                }
                            }

                            // DIA Box
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFFFF1F2))
                                    .border(1.dp, Color(0xFFFFE4E6), RoundedCornerShape(14.dp))
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "DIA", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = uiState.diastolic.toString(),
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BpCrimson
                                    )
                                    Text(text = "mmHg", fontSize = 11.sp, color = TextSecondary)
                                    Text(text = "Diastolic", fontSize = 10.sp, color = TextLight)
                                }
                            }

                            // PULSE Box
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFF0FDF4))
                                    .border(1.dp, Color(0xFFCCFBF1), RoundedCornerShape(14.dp))
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "♥ PULSE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = uiState.heartRate.toString(),
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Text(text = "bpm", fontSize = 11.sp, color = TextSecondary)
                                    Text(text = "Heart Beat", fontSize = 10.sp, color = TextLight)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Arm & Posture Context Selector
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AirlineSeatReclineNormal,
                                        contentDescription = null,
                                        tint = BpCrimson,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(text = "Arm & Posture Context", fontSize = 10.sp, color = TextSecondary)
                                        Text(
                                            text = uiState.postureContext,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BpCrimson
                                        )
                                    }
                                }

                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Save BP Record Button
                        Button(
                            onClick = { viewModel.saveRecord() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .shadow(2.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BpCrimson)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Save BP Record",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // 4. Detailed BP Log Section
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.History,
                                contentDescription = null,
                                tint = BpCrimson,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Detailed BP Log",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { }
                        ) {
                            Text(
                                text = "Export PDF",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = BpCrimson
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.FileDownload,
                                contentDescription = null,
                                tint = BpCrimson,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Group: TODAY, OCT 24, 2024
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TODAY, OCT 24, 2024",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF880023),
                            letterSpacing = 0.5.sp
                        )
                        Text(text = "2 Logs", fontSize = 11.sp, color = TextSecondary)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Entry 1: Robert Miller 124 / 82
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
                                            .clip(CircleShape)
                                            .background(Color(0xFFFFE4E6)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "RM", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = "Robert Miller • 8:15 AM", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = "Morning Baseline • Left Arm", fontSize = 11.sp, color = TextSecondary)
                                    }
                                }

                                StatusBadge(text = "Optimal")
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(text = "BP ", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                    Text(text = "124 / 82", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                    Text(text = " mmHg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "♥", fontSize = 12.sp, color = BpCrimson)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "72 bpm", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Entry 2: Elena Miller 118 / 78
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
                                            .clip(CircleShape)
                                            .background(Color(0xFFE0F2FE)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "EM", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0369A1))
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = "Elena Miller • 8:30 AM", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = "Post-Meditation • Right Arm", fontSize = 11.sp, color = TextSecondary)
                                    }
                                }

                                StatusBadge(text = "Normal")
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(text = "BP ", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                    Text(text = "118 / 78", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                    Text(text = " mmHg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "♥", fontSize = 12.sp, color = BpCrimson)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "68 bpm", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Group: YESTERDAY, OCT 23, 2024
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "YESTERDAY, OCT 23, 2024",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF880023),
                            letterSpacing = 0.5.sp
                        )
                        Text(text = "2 Logs", fontSize = 11.sp, color = TextSecondary)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Yesterday Entry 1: Robert Miller 128 / 84
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
                                            .clip(CircleShape)
                                            .background(Color(0xFFFFE4E6)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "RM", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = "Robert Miller • 7:50 PM", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = "Evening Relax • Left Arm", fontSize = 11.sp, color = TextSecondary)
                                    }
                                }

                                StatusBadge(text = "Normal")
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(text = "BP ", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                    Text(text = "128 / 84", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                    Text(text = " mmHg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "♥", fontSize = 12.sp, color = BpCrimson)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "75 bpm", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Yesterday Entry 2: Robert Miller 122 / 80
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
                                            .clip(CircleShape)
                                            .background(Color(0xFFFFE4E6)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "RM", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = "Robert Miller • 8:00 AM", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = "Morning Routine • Left Arm", fontSize = 11.sp, color = TextSecondary)
                                    }
                                }

                                StatusBadge(text = "Optimal")
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(text = "BP ", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                    Text(text = "122 / 80", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                    Text(text = " mmHg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "♥", fontSize = 12.sp, color = BpCrimson)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "70 bpm", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BpCrimson)
                                }
                            }
                        }
                    }
                }
            }

            // 5. Cardio Routine Tip Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(Color(0xFFFFF1F2))
                        .border(1.dp, Color(0xFFFFE4E6), CardShape)
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFE4E6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                tint = BpCrimson,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Cardio Routine Tip",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Robert's readings remain consistently within standard healthy systolic thresholds (<130 mmHg).",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp
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
