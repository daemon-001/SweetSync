package com.daemon.sweetsync.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daemon.sweetsync.data.model.TrackType
import com.daemon.sweetsync.ui.theme.AppPrimary
import com.daemon.sweetsync.ui.theme.AppPrimaryTint
import com.daemon.sweetsync.ui.theme.BpDot
import com.daemon.sweetsync.ui.theme.CardShape
import com.daemon.sweetsync.ui.theme.CardStroke
import com.daemon.sweetsync.ui.theme.PillShape
import com.daemon.sweetsync.ui.theme.SugarDot
import com.daemon.sweetsync.ui.theme.TextLight
import com.daemon.sweetsync.ui.theme.TextPrimary
import com.daemon.sweetsync.ui.theme.TextSecondary
import com.daemon.sweetsync.ui.theme.WeightDot

@Composable
fun MultiTrackCalendar(
    selectedDate: String, // "yyyy-MM-dd", e.g. "2024-10-24"
    onDateSelected: (String) -> Unit,
    trackRecordsMap: Map<String, Set<String>>, // "2024-10-24" -> Set("BLOOD_SUGAR", "BLOOD_PRESSURE", "WEIGHT")
    modifier: Modifier = Modifier
) {
    var currentYear by remember { mutableStateOf(2024) }
    var currentMonth by remember { mutableStateOf(10) } // October

    val monthName = "October"
    val daysInWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    // Calendar grid structure for October 2024:
    // Oct 1 was Tuesday. So 1 trailing day from Sept (30) in Monday slot.
    // 31 days in October.
    data class CalendarDay(
        val dayNumber: Int,
        val isCurrentMonth: Boolean,
        val fullDateString: String
    )

    val gridDays = remember(currentYear, currentMonth) {
        val list = mutableListOf<CalendarDay>()
        // Mon 30 Sept
        list.add(CalendarDay(30, false, "2024-09-30"))
        // Oct 1 to Oct 31
        for (d in 1..31) {
            val dateStr = String.format(java.util.Locale.US, "%04d-%02d-%02d", currentYear, currentMonth, d)
            list.add(CalendarDay(d, true, dateStr))
        }
        // trailing 3 days for Nov (1, 2, 3) to fill the 5x7 grid
        list.add(CalendarDay(1, false, "2024-11-01"))
        list.add(CalendarDay(2, false, "2024-11-02"))
        list.add(CalendarDay(3, false, "2024-11-03"))
        list
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(Color.White)
            .border(1.dp, CardStroke, CardShape)
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Month & Year + Navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$monthName $currentYear",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(Color(0xFF005B52), CircleShape)
                        )
                    }
                    Text(
                        text = "Daily metabolic logs across tracks",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous Month",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(PillShape)
                            .background(AppPrimaryTint)
                            .clickable {
                                onDateSelected("2024-10-24")
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Today",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AppPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next Month",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Weekdays row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                daysInWeek.forEach { day ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Days Grid (5 rows of 7 days)
            val rows = gridDays.chunked(7)
            rows.forEach { week ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    week.forEach { calDay ->
                        val isSelected = calDay.fullDateString == selectedDate
                        val tracks = trackRecordsMap[calDay.fullDateString] ?: emptySet()

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(enabled = calDay.isCurrentMonth) {
                                    onDateSelected(calDay.fullDateString)
                                }
                                .then(
                                    if (isSelected) {
                                        Modifier.background(Color(0xFF2DD4BF).copy(alpha = 0.35f))
                                    } else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                if (isSelected) {
                                    // Highlight pill container matching design screenshot
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(AppPrimary, RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = calDay.dayNumber.toString(),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                } else {
                                    Text(
                                        text = calDay.dayNumber.toString(),
                                        fontSize = 13.sp,
                                        fontWeight = if (calDay.isCurrentMonth) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (calDay.isCurrentMonth) TextPrimary else TextLight
                                    )
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                // Colored dots for active tracks
                                if (calDay.isCurrentMonth && tracks.isNotEmpty()) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (tracks.contains(TrackType.BLOOD_SUGAR.code)) {
                                            Box(
                                                modifier = Modifier
                                                    .size(4.dp)
                                                    .background(SugarDot, CircleShape)
                                            )
                                        }
                                        if (tracks.contains(TrackType.BLOOD_PRESSURE.code)) {
                                            Box(
                                                modifier = Modifier
                                                    .size(4.dp)
                                                    .background(BpDot, CircleShape)
                                            )
                                        }
                                        if (tracks.contains(TrackType.WEIGHT.code)) {
                                            Box(
                                                modifier = Modifier
                                                    .size(4.dp)
                                                    .background(WeightDot, CircleShape)
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

            Spacer(modifier = Modifier.height(16.dp))

            // Calendar Legend pill
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1F5F9), PillShape)
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(SugarDot, CircleShape))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "Blood Sugar", fontSize = 11.sp, color = TextSecondary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(BpDot, CircleShape))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "Blood Pressure", fontSize = 11.sp, color = TextSecondary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(WeightDot, CircleShape))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "Body Weight", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }
        }
    }
}
