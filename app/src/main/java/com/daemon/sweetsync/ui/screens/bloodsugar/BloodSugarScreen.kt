package com.daemon.sweetsync.ui.screens.bloodsugar

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daemon.sweetsync.R
import com.daemon.sweetsync.ui.components.GlycemicSplineChart
import com.daemon.sweetsync.ui.components.MetricStepper
import com.daemon.sweetsync.ui.components.StatusBadge
import com.daemon.sweetsync.ui.components.SweetSyncDetailTopBar
import com.daemon.sweetsync.ui.theme.AppPrimary
import com.daemon.sweetsync.ui.theme.AppPrimaryTint
import com.daemon.sweetsync.ui.theme.CardShape
import com.daemon.sweetsync.ui.theme.CardStroke
import com.daemon.sweetsync.ui.theme.CardSurface
import com.daemon.sweetsync.ui.theme.InnerCardShape
import com.daemon.sweetsync.ui.theme.PillShape
import com.daemon.sweetsync.ui.theme.ScreenBackground
import com.daemon.sweetsync.ui.theme.StatusOptimalBg
import com.daemon.sweetsync.ui.theme.StatusOptimalText
import com.daemon.sweetsync.ui.theme.SugarBg
import com.daemon.sweetsync.ui.theme.SugarDot
import com.daemon.sweetsync.ui.theme.SugarOrange
import com.daemon.sweetsync.ui.theme.SugarOrangeDark
import com.daemon.sweetsync.ui.theme.TextLight
import com.daemon.sweetsync.ui.theme.TextPrimary
import com.daemon.sweetsync.ui.theme.TextSecondary

@Composable
fun BloodSugarScreen(
    viewModel: BloodSugarViewModel,
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
            title = "Blood Sugar Records",
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
                                .background(Color(0xFFFFEDD5), PillShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "BLOOD SUGAR TRACK • MG/DL",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SugarOrange
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(AppPrimaryTint, PillShape)
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(5.dp).background(AppPrimary, CircleShape))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Live Sync Active",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Member Filter Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val filters = listOf("All Family", "Robert Miller", "Elena Miller")
                        filters.forEach { filter ->
                            val isSel = uiState.selectedMemberFilter == filter
                            Box(
                                modifier = Modifier
                                    .clip(PillShape)
                                    .background(
                                        if (isSel) SugarOrangeDark else Color(0xFFF1F5F9)
                                    )
                                    .clickable { viewModel.onFilterChange(filter) }
                                    .padding(horizontal = 14.dp, vertical = 7.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isSel && filter != "All Family") {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
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

            // 2. 7-Day Glycemic Baseline Overview Card
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
                            Column {
                                Text(
                                    text = "7-DAY GLYCEMIC BASELINE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Target Stability: 94% In-Range",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFEDD5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.ShowChart,
                                    contentDescription = null,
                                    tint = SugarOrange,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Dual Stats Box
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Avg Fasting
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(InnerCardShape)
                                    .background(Color(0xFFF8FAFC))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.WbSunny,
                                            contentDescription = null,
                                            tint = Color(0xFFD97706),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Avg Fasting", fontSize = 11.sp, color = TextSecondary)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "98", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = " mg/dL", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = "↓ 3 mg/dL vs last week", fontSize = 11.sp, color = AppPrimary, fontWeight = FontWeight.Medium)
                                }
                            }

                            // Avg Post-Lunch
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(InnerCardShape)
                                    .background(Color(0xFFF8FAFC))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Restaurant,
                                            contentDescription = null,
                                            tint = SugarOrange,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Avg Post-Lunch", fontSize = 11.sp, color = TextSecondary)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "134", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = " mg/dL", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = "Optimal postprandial", fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Spline Chart
                        GlycemicSplineChart()
                    }
                }
            }

            // 3. Add Blood Sugar Record Section
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Header Expandable Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFEA580C), Color(0xFFC2410C))
                                )
                            )
                            .clickable { viewModel.toggleAddSection() }
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "+ Add Blood Sugar Record",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Fasting, post-meal or bedtime reading",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                }
                            }

                            Icon(
                                imageVector = if (uiState.isAddEntryExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // Form Card (When Expanded)
                    AnimatedVisibility(visible = uiState.isAddEntryExpanded) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
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
                                        Box(modifier = Modifier.size(7.dp).background(SugarDot, CircleShape))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "New Blood Sugar Entry",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFFFEDD5), PillShape)
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "Robert Selected",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = SugarOrange
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Logged Timestamp row
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFFF8FAFC))
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Outlined.CalendarMonth,
                                                contentDescription = null,
                                                tint = SugarOrange,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(text = "Logged Timestamp", fontSize = 11.sp, color = TextSecondary)
                                                Text(
                                                    text = uiState.timestampText,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary
                                                )
                                            }
                                        }

                                        Box(
                                            modifier = Modifier
                                                .clip(PillShape)
                                                .background(Color(0xFFE0F2FE))
                                                .clickable { }
                                                .padding(horizontal = 12.dp, vertical = 6.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Edit",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0284C7)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Selected Member Chips
                                Text(
                                    text = "Selected Member",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val memberChips = listOf(
                                        Pair("mem_robert_1", "Robert M."),
                                        Pair("mem_elena_2", "Elena M."),
                                        Pair("mem_sarah_4", "Sarah")
                                    )

                                    memberChips.forEach { (id, label) ->
                                        val isSel = uiState.selectedMemberId == id
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(42.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                    if (isSel) SugarOrangeDark else Color(0xFFF1F5F9)
                                                )
                                                .clickable { viewModel.onMemberSelected(id) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 12.sp,
                                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSel) Color.White else TextPrimary
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Metric 1: Blood Sugar in Fasting
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(InnerCardShape)
                                        .background(Color(0xFFF8FAFC))
                                        .padding(14.dp)
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
                                                    tint = Color(0xFFD97706),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Blood Sugar in Fasting",
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary
                                                )
                                            }
                                            Text(
                                                text = "Morning (8+ hrs rest)",
                                                fontSize = 11.sp,
                                                color = TextSecondary
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        MetricStepper(
                                            value = uiState.fastingValue,
                                            unit = "mg/dL",
                                            step = 1,
                                            onValueChange = { viewModel.onFastingChange(it) }
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "✓ Normal fasting range: 70 - 99 mg/dL",
                                            fontSize = 11.sp,
                                            color = AppPrimary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Metric 2: Blood Sugar after Lunch
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(InnerCardShape)
                                        .background(Color(0xFFF8FAFC))
                                        .padding(14.dp)
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
                                                    tint = SugarOrange,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Blood Sugar after Lunch",
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary
                                                )
                                            }
                                            Text(
                                                text = "Taken 2 hrs post meal",
                                                fontSize = 11.sp,
                                                color = SugarOrange,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        MetricStepper(
                                            value = uiState.postLunchValue,
                                            unit = "mg/dL",
                                            step = 5,
                                            onValueChange = { viewModel.onPostLunchChange(it) }
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "Target under 140 mg/dL for non-diabetic metabolic health",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Metabolic Context / Meal Tags
                                Text(
                                    text = "Metabolic Context / Meal Tags",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val tags = listOf("Before Breakfast", "Post-Lunch", "Fasting (8+ hrs)", "Bedtime")
                                    val row1 = tags.take(2)
                                    val row2 = tags.drop(2)

                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            row1.forEach { tag ->
                                                val isSel = uiState.selectedMealTag == tag
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(38.dp)
                                                        .clip(PillShape)
                                                        .background(
                                                            if (isSel) SugarOrangeDark else Color(0xFFF1F5F9)
                                                        )
                                                        .clickable { viewModel.onMealTagSelected(tag) },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = tag,
                                                        fontSize = 12.sp,
                                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                                        color = if (isSel) Color.White else TextPrimary
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            row2.forEach { tag ->
                                                val isSel = uiState.selectedMealTag == tag
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(38.dp)
                                                        .clip(PillShape)
                                                        .background(
                                                            if (isSel) SugarOrangeDark else Color(0xFFF1F5F9)
                                                        )
                                                        .clickable { viewModel.onMealTagSelected(tag) },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = tag,
                                                        fontSize = 12.sp,
                                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                                        color = if (isSel) Color.White else TextPrimary
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Quick Meal Note
                                Text(
                                    text = "Quick Meal Note",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF1F5F9))
                                        .padding(horizontal = 12.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    OutlinedTextField(
                                        value = uiState.mealNote,
                                        onValueChange = { viewModel.onMealNoteChange(it) },
                                        placeholder = { Text("e.g. Avocado toast with poached egg and black tea", fontSize = 12.sp) },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent,
                                            focusedTextColor = TextPrimary,
                                            unfocusedTextColor = TextPrimary
                                        ),
                                        singleLine = true
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Save Record Button
                                Button(
                                    onClick = { viewModel.saveRecord() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .shadow(2.dp, RoundedCornerShape(16.dp)),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SugarOrange)
                                ) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Save Record",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Log Timeline Section
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
                                tint = SugarOrange,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Log Timeline",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "Showing 5 entries",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
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
                            color = Color(0xFF9A3412),
                            letterSpacing = 0.5.sp
                        )
                        Text(text = "3 Readings", fontSize = 11.sp, color = TextSecondary)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Entry 1: 138 mg/dL Post-Lunch
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
                                            imageVector = Icons.Default.Restaurant,
                                            contentDescription = null,
                                            tint = SugarOrange,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Row(verticalAlignment = Alignment.Bottom) {
                                            Text(text = "138", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            Text(text = " mg/dL", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            StatusBadge(text = "Normal")
                                        }
                                        Text(text = "Post-Lunch • 2:10 PM", fontSize = 11.sp, color = TextSecondary)
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = "Robert Miller", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text(text = "2h Postprandial", fontSize = 11.sp, color = TextSecondary)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "≡ After grilled chicken salad & sparkling water",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Entry 2: 104 mg/dL Fasting Robert
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(CardSurface)
                            .border(1.dp, CardStroke, CardShape)
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
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFFFEDD5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WbSunny,
                                        contentDescription = null,
                                        tint = Color(0xFFD97706),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "104", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = " mg/dL", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        StatusBadge(text = "Target Normal")
                                    }
                                    Text(text = "In Fasting • 7:45 AM", fontSize = 11.sp, color = TextSecondary)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "Robert Miller", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(text = "9h Fast", fontSize = 11.sp, color = TextSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Entry 3: 96 mg/dL Fasting Elena
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(CardSurface)
                            .border(1.dp, CardStroke, CardShape)
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
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFCCFBF1)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Opacity,
                                        contentDescription = null,
                                        tint = AppPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "96", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = " mg/dL", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        StatusBadge(text = "Target Optimal", textColor = StatusOptimalText, backgroundColor = StatusOptimalBg)
                                    }
                                    Text(text = "In Fasting • 8:00 AM", fontSize = 11.sp, color = TextSecondary)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "Elena Miller", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(text = "Family Sync", fontSize = 11.sp, color = TextSecondary)
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
                            color = Color(0xFF9A3412),
                            letterSpacing = 0.5.sp
                        )
                        Text(text = "2 Readings", fontSize = 11.sp, color = TextSecondary)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Yesterday Entry 1: 142 mg/dL
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(CardSurface)
                            .border(1.dp, CardStroke, CardShape)
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
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFFFEDD5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Restaurant,
                                        contentDescription = null,
                                        tint = SugarOrange,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "142", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = " mg/dL", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        StatusBadge(text = "Normal")
                                    }
                                    Text(text = "Post-Lunch • 1:50 PM", fontSize = 11.sp, color = TextSecondary)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "Robert Miller", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(text = "Quinoa bowl", fontSize = 11.sp, color = TextSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Yesterday Entry 2: 101 mg/dL
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CardShape)
                            .background(CardSurface)
                            .border(1.dp, CardStroke, CardShape)
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
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFFFEDD5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WbSunny,
                                        contentDescription = null,
                                        tint = Color(0xFFD97706),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "101", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = " mg/dL", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        StatusBadge(text = "Normal")
                                    }
                                    Text(text = "In Fasting • 7:30 AM", fontSize = 11.sp, color = TextSecondary)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "Robert Miller", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(text = "Baseline", fontSize = 11.sp, color = TextSecondary)
                            }
                        }
                    }
                }
            }

            // 5. Dr. Thorne's Glycemic Tip Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(Color(0xFFEFF6FF))
                        .border(1.dp, Color(0xFFDBEAFE), CardShape)
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_doctor),
                            contentDescription = "Dr. Thorne",
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Dr. Thorne's Glycemic Tip",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Robert's post-lunch stability improved by 8% this week. Keep up the high-fiber grain and legume meal structure.",
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
