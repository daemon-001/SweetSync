package com.sweetsync.app.ui.screens.weight

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
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.sweetsync.app.ui.components.StatusBadge
import com.sweetsync.app.ui.components.SweetSyncDetailTopBar
import com.sweetsync.app.ui.theme.AppPrimary
import com.sweetsync.app.ui.theme.AppPrimaryTint
import com.sweetsync.app.ui.theme.CardShape
import com.sweetsync.app.ui.theme.CardStroke
import com.sweetsync.app.ui.theme.CardSurface
import com.sweetsync.app.ui.theme.InnerCardShape
import com.sweetsync.app.ui.theme.PillShape
import com.sweetsync.app.ui.theme.ScreenBackground
import com.sweetsync.app.ui.theme.StatusNormalBg
import com.sweetsync.app.ui.theme.StatusNormalText
import com.sweetsync.app.ui.theme.StatusOptimalBg
import com.sweetsync.app.ui.theme.StatusOptimalText
import com.sweetsync.app.ui.theme.TextLight
import com.sweetsync.app.ui.theme.TextPrimary
import com.sweetsync.app.ui.theme.TextSecondary
import com.sweetsync.app.ui.theme.WeightTeal

@Composable
fun WeightScreen(
    viewModel: WeightViewModel,
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
            title = "Weight Records",
            onBackClick = onNavigateBack,
            onProfileClick = onNavigateToProfile
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Header Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(Color(0xFFF0FDF4).copy(alpha = 0.7f))
                        .border(1.dp, Color(0xFFCCFBF1), CardShape)
                        .padding(16.dp)
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
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Scale,
                                        contentDescription = null,
                                        tint = WeightTeal,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "METABOLIC PILLAR",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextSecondary,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = "Weight Track • kg & BMI",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .background(Color.White, PillShape)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(5.dp).background(Color(0xFF10B981), CircleShape))
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "Calibrated",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Filter chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val filters = listOf("All Family", "Elena Miller", "Lucas Miller")
                            filters.forEach { filter ->
                                val isSel = uiState.selectedMemberFilter == filter
                                Box(
                                    modifier = Modifier
                                        .clip(PillShape)
                                        .background(if (isSel) AppPrimary else Color.White)
                                        .clickable { viewModel.onFilterChange(filter) }
                                        .padding(horizontal = 14.dp, vertical = 7.dp),
                                    contentAlignment = Alignment.Center
                                ) {
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

            // 2. Target Horizon Card
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
                            Text(
                                text = "Target Horizon",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFE0F2FE), PillShape)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(text = "In Motion", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0284C7))
                                }
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFE0F2FE), PillShape)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(text = "📉 -0.4 kg this week", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0284C7))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Dual Stat Boxes
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Current Reading
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(InnerCardShape)
                                    .background(Color(0xFFF8FAFC))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(text = "Current Reading", fontSize = 11.sp, color = TextSecondary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "64.2", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = " kg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = "🌱 BMI 21.8 • Normal", fontSize = 11.sp, color = StatusOptimalText, fontWeight = FontWeight.Medium)
                                }
                            }

                            // Target Goal
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(InnerCardShape)
                                    .background(Color(0xFFF8FAFC))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(text = "Target Goal", fontSize = 11.sp, color = TextSecondary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "62.0", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = " kg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = "🏁 2.2 kg to milestone", fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Progress Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Starting: 66.8 kg", fontSize = 11.sp, color = TextSecondary)
                            Text(text = "Goal: 62.0 kg", fontSize = 11.sp, color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { 0.54f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(PillShape),
                            color = AppPrimary,
                            trackColor = Color(0xFFE2E8F0)
                        )
                    }
                }
            }

            // 3. New Weight Entry Card
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
                            Text(
                                text = "New Weight Entry",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Sync,
                                    contentDescription = null,
                                    tint = AppPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Auto-Sync Ready",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AppPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Member Profile & Timestamp Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFF1F5F9))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE0F2FE)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "S", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AppPrimary)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = "Sarah Miller", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = "Family Profile", fontSize = 10.sp, color = TextSecondary)
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = AppPrimary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Today, Oct 24 • 07:15 AM",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Metric Standard Segmented Toggle: [ kg | lbs ]
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Metric Standard",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )

                            Row(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(Color(0xFFF1F5F9))
                                    .padding(3.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val isKg = uiState.unit == "kg"
                                Box(
                                    modifier = Modifier
                                        .clip(PillShape)
                                        .background(if (isKg) AppPrimary else Color.Transparent)
                                        .clickable { viewModel.onUnitChange("kg") }
                                        .padding(horizontal = 14.dp, vertical = 5.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "kg",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isKg) Color.White else TextSecondary
                                    )
                                }

                                val isLbs = uiState.unit == "lbs"
                                Box(
                                    modifier = Modifier
                                        .clip(PillShape)
                                        .background(if (isLbs) AppPrimary else Color.Transparent)
                                        .clickable { viewModel.onUnitChange("lbs") }
                                        .padding(horizontal = 14.dp, vertical = 5.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "lbs",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isLbs) Color.White else TextSecondary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Dual Metric Inputs: Fasting AM | Post-Lunch PM
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Fasting AM Card
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(InnerCardShape)
                                    .background(Color(0xFFF8FAFC))
                                    .border(1.dp, CardStroke, InnerCardShape)
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
                                                imageVector = Icons.Default.WbSunny,
                                                contentDescription = null,
                                                tint = Color(0xFF0284C7),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "Fasting", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        }
                                        Text(text = "AM", fontSize = 11.sp, color = TextSecondary)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = uiState.fastingWeight.toString(),
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "Empty stomach", fontSize = 11.sp, color = TextLight)
                                }
                            }

                            // Post-Lunch PM Card
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(InnerCardShape)
                                    .background(Color(0xFFF8FAFC))
                                    .border(1.dp, CardStroke, InnerCardShape)
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
                                                imageVector = Icons.Default.Restaurant,
                                                contentDescription = null,
                                                tint = Color(0xFFD97706),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "Post-Lunch", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9A3412))
                                        }
                                        Text(text = "PM", fontSize = 11.sp, color = TextSecondary)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = uiState.postLunchWeight.toString(),
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "Post-meal baseline", fontSize = 11.sp, color = TextLight)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Context Factors
                        Text(
                            text = "Context Factors",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val factor1 = "Post morning walk"
                            val isF1 = uiState.selectedContextFactors.contains(factor1)
                            Box(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(if (isF1) Color(0xFFE0F2FE) else Color(0xFFF1F5F9))
                                    .clickable { viewModel.toggleFactor(factor1) }
                                    .padding(horizontal = 12.dp, vertical = 7.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                                        contentDescription = null,
                                        tint = if (isF1) Color(0xFF0284C7) else TextSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = factor1,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isF1) Color(0xFF0284C7) else TextPrimary
                                    )
                                }
                            }

                            val factor2 = "Well hydrated"
                            val isF2 = uiState.selectedContextFactors.contains(factor2)
                            Box(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(if (isF2) Color(0xFFE0F2FE) else Color(0xFFF1F5F9))
                                    .clickable { viewModel.toggleFactor(factor2) }
                                    .padding(horizontal = 12.dp, vertical = 7.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Opacity,
                                        contentDescription = null,
                                        tint = if (isF2) Color(0xFF0284C7) else TextSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = factor2,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isF2) Color(0xFF0284C7) else TextPrimary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(Color(0xFFF1F5F9))
                                    .clickable { }
                                    .padding(horizontal = 10.dp, vertical = 7.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+ Add note",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Save Weight Record Button
                        Button(
                            onClick = { viewModel.saveRecord() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .shadow(2.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AppPrimary)
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Save Weight Record",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // 4. Chronological Records
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Chronological Records",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Last 7 Days",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Group: Today, Oct 24, 2024
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = AppPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Today, Oct 24, 2024",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Entry 1: Sarah Miller Fasting 64.2 kg
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
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE0F2FE)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "S", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppPrimary)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = "Sarah Miller", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = "Logged at 07:15 AM • Fasting", fontSize = 11.sp, color = TextSecondary)
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFE0F2FE), PillShape)
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(text = "Primary", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0284C7))
                                    }
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFE0F2FE), PillShape)
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(text = "↓ -0.2 kg vs yesterday", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0284C7))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Morning Fasting", fontSize = 12.sp, color = TextSecondary)
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "64.2", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = " kg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Entry 2: Lucas Miller Fasting 72.8 kg / After Lunch 73.5 kg
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
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE0F2FE)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "L", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = "Lucas Miller", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = "Logged at 02:15 PM • Complete Log", fontSize = 11.sp, color = TextSecondary)
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFF1F5F9), PillShape)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(text = "Stable", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFF8FAFC))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(text = "Fasting AM", fontSize = 11.sp, color = TextSecondary)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(verticalAlignment = Alignment.Bottom) {
                                            Text(text = "72.8", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            Text(text = " kg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFF8FAFC))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(text = "After Lunch PM", fontSize = 11.sp, color = TextSecondary)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(verticalAlignment = Alignment.Bottom) {
                                            Text(text = "73.5", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            Text(text = " kg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Group: Oct 23, 2024
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Oct 23, 2024",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Entry 1: Sarah Miller Oct 23
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
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE0F2FE)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "S", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AppPrimary)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = "Sarah Miller", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = "Logged at 01:45 PM • Dual Track", fontSize = 11.sp, color = TextSecondary)
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFF1F5F9), PillShape)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(text = "Baseline", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFF8FAFC))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(text = "Fasting", fontSize = 11.sp, color = TextSecondary)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(verticalAlignment = Alignment.Bottom) {
                                            Text(text = "64.4", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            Text(text = " kg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFF8FAFC))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(text = "After Lunch", fontSize = 11.sp, color = TextSecondary)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(verticalAlignment = Alignment.Bottom) {
                                            Text(text = "65.1", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            Text(text = " kg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Entry 2: Elena Miller Oct 23
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
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE0F2FE)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "E", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0369A1))
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = "Elena Miller", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = "Logged at 07:30 AM • Fasting", fontSize = 11.sp, color = TextSecondary)
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFD1FAE5), PillShape)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(text = "Optimal", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StatusOptimalText)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Morning Fasting", fontSize = 12.sp, color = TextSecondary)
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "64.6", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = " kg", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                    }
                                }
                            }
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
