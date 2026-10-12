package com.sweetsync.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sweetsync.app.ui.theme.TextPrimary
import com.sweetsync.app.ui.theme.TextSecondary

@Composable
fun MetricStepper(
    value: Double,
    unit: String,
    step: Int = 1,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    isIntegerDisplay: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = if (isIntegerDisplay) value.toInt().toString() else String.format(java.util.Locale.US, "%.1f", value),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = " $unit",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decrement button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFEFF6FF))
                    .clickable {
                        val next = (value - step).coerceAtLeast(0.0)
                        onValueChange(next)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "-$step",
                    color = Color(0xFF1E3A8A),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Increment button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFEFF6FF))
                    .clickable {
                        onValueChange(value + step)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+$step",
                    color = Color(0xFF1E3A8A),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
