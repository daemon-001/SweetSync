package com.sweetsync.app.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.sweetsync.app.data.model.FamilyMember
import com.sweetsync.app.data.model.HealthRecord
import com.sweetsync.app.data.model.StatusLevel
import com.sweetsync.app.data.model.TrackType
import com.sweetsync.app.ui.theme.AppPrimary
import com.sweetsync.app.ui.theme.BpBg
import com.sweetsync.app.ui.theme.BpCrimson
import com.sweetsync.app.ui.theme.CardSurface
import com.sweetsync.app.ui.theme.PillShape
import com.sweetsync.app.ui.theme.SugarBg
import com.sweetsync.app.ui.theme.SugarOrange
import com.sweetsync.app.ui.theme.TextPrimary
import com.sweetsync.app.ui.theme.TextSecondary
import com.sweetsync.app.ui.theme.WeightBg
import com.sweetsync.app.ui.theme.WeightTeal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickLogVitalSheet(
    familyMembers: List<FamilyMember>,
    initialMemberId: String? = null,
    onDismiss: () -> Unit,
    onSaveRecord: (HealthRecord) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedMemberId by remember {
        mutableStateOf(initialMemberId ?: familyMembers.firstOrNull()?.id ?: "")
    }
    var selectedTrack by remember { mutableStateOf(TrackType.BLOOD_SUGAR) }

    // Sugar inputs
    var sugarFasting by remember { mutableDoubleStateOf(95.0) }
    var sugarPostLunch by remember { mutableDoubleStateOf(132.0) }
    var sugarMealTag by remember { mutableStateOf("Post-Lunch") }
    var sugarMealNote by remember { mutableStateOf("") }

    // BP inputs
    var systolic by remember { mutableIntStateOf(120) }
    var diastolic by remember { mutableIntStateOf(80) }
    var pulse by remember { mutableIntStateOf(72) }

    // Weight inputs
    var weightFasting by remember { mutableDoubleStateOf(64.2) }
    var weightPostLunch by remember { mutableDoubleStateOf(64.9) }
    var weightUnit by remember { mutableStateOf("kg") }

    val selectedMember = familyMembers.find { it.id == selectedMemberId } ?: familyMembers.firstOrNull()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CardSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quick Log Vital",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Member selector pills
            Text(
                text = "Select Family Member",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                familyMembers.take(4).forEach { member ->
                    val isSel = member.id == selectedMemberId
                    Box(
                        modifier = Modifier
                            .clip(PillShape)
                            .background(if (isSel) AppPrimary else Color(0xFFF1F5F9))
                            .clickable { selectedMemberId = member.id }
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = member.fullName.split(" ").first(),
                            fontSize = 12.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSel) Color.White else TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Track selector
            Text(
                text = "Health Metric Track",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Sugar
                val isSugar = selectedTrack == TrackType.BLOOD_SUGAR
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSugar) SugarBg else Color(0xFFF8FAFC))
                        .clickable { selectedTrack = TrackType.BLOOD_SUGAR }
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Opacity,
                            contentDescription = null,
                            tint = SugarOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sugar",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSugar) SugarOrange else TextSecondary
                        )
                    }
                }

                // BP
                val isBp = selectedTrack == TrackType.BLOOD_PRESSURE
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isBp) BpBg else Color(0xFFF8FAFC))
                        .clickable { selectedTrack = TrackType.BLOOD_PRESSURE }
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = null,
                            tint = BpCrimson,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Vascular",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isBp) BpCrimson else TextSecondary
                        )
                    }
                }

                // Weight
                val isWeight = selectedTrack == TrackType.WEIGHT
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isWeight) WeightBg else Color(0xFFF8FAFC))
                        .clickable { selectedTrack = TrackType.WEIGHT }
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Scale,
                            contentDescription = null,
                            tint = WeightTeal,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Weight",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isWeight) WeightTeal else TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTrack) {
                TrackType.BLOOD_SUGAR -> {
                    Text(text = "Blood Sugar in Fasting (Morning)", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    MetricStepper(
                        value = sugarFasting,
                        unit = "mg/dL",
                        step = 1,
                        onValueChange = { sugarFasting = it }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Blood Sugar after Lunch", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    MetricStepper(
                        value = sugarPostLunch,
                        unit = "mg/dL",
                        step = 5,
                        onValueChange = { sugarPostLunch = it }
                    )
                }

                TrackType.BLOOD_PRESSURE -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "SYS (mmHg)", fontSize = 11.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            MetricStepper(
                                value = systolic.toDouble(),
                                unit = "",
                                step = 1,
                                onValueChange = { systolic = it.toInt() }
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "DIA (mmHg)", fontSize = 11.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            MetricStepper(
                                value = diastolic.toDouble(),
                                unit = "",
                                step = 1,
                                onValueChange = { diastolic = it.toInt() }
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "PULSE (bpm)", fontSize = 11.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            MetricStepper(
                                value = pulse.toDouble(),
                                unit = "",
                                step = 1,
                                onValueChange = { pulse = it.toInt() }
                            )
                        }
                    }
                }

                TrackType.WEIGHT -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Fasting AM ($weightUnit)", fontSize = 11.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            MetricStepper(
                                value = weightFasting,
                                unit = "",
                                step = 1,
                                isIntegerDisplay = false,
                                onValueChange = { weightFasting = it }
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Post-Lunch PM ($weightUnit)", fontSize = 11.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            MetricStepper(
                                value = weightPostLunch,
                                unit = "",
                                step = 1,
                                isIntegerDisplay = false,
                                onValueChange = { weightPostLunch = it }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Save button
            val btnColor = when (selectedTrack) {
                TrackType.BLOOD_SUGAR -> SugarOrange
                TrackType.BLOOD_PRESSURE -> BpCrimson
                TrackType.WEIGHT -> AppPrimary
            }

            Button(
                onClick = {
                    val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    val sdfTime = SimpleDateFormat("hh:mm a", Locale.US)
                    val now = Date()

                    val record = HealthRecord(
                        memberId = selectedMemberId,
                        memberName = selectedMember?.fullName ?: "Member",
                        trackType = selectedTrack.code,
                        timestamp = now.time,
                        dateString = sdfDate.format(now),
                        timeString = sdfTime.format(now),
                        sugarFasting = if (selectedTrack == TrackType.BLOOD_SUGAR) sugarFasting else null,
                        sugarPostLunch = if (selectedTrack == TrackType.BLOOD_SUGAR) sugarPostLunch else null,
                        mealTag = if (selectedTrack == TrackType.BLOOD_SUGAR) sugarMealTag else null,
                        mealNote = if (selectedTrack == TrackType.BLOOD_SUGAR && sugarMealNote.isNotBlank()) sugarMealNote else null,
                        systolic = if (selectedTrack == TrackType.BLOOD_PRESSURE) systolic else null,
                        diastolic = if (selectedTrack == TrackType.BLOOD_PRESSURE) diastolic else null,
                        heartRate = if (selectedTrack == TrackType.BLOOD_PRESSURE) pulse else null,
                        postureContext = if (selectedTrack == TrackType.BLOOD_PRESSURE) "Left Arm • Sitting • Resting 5 min" else null,
                        bpTag = if (selectedTrack == TrackType.BLOOD_PRESSURE) "Clinical Reading" else null,
                        weightFasting = if (selectedTrack == TrackType.WEIGHT) weightFasting else null,
                        weightPostLunch = if (selectedTrack == TrackType.WEIGHT) weightPostLunch else null,
                        unit = weightUnit,
                        statusText = "Optimal",
                        statusLevel = StatusLevel.OPTIMAL
                    )
                    onSaveRecord(record)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = btnColor)
            ) {
                Icon(imageVector = Icons.Outlined.Check, contentDescription = null, tint = Color.White)
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
