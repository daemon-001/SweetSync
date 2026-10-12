package com.daemon.sweetsync.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daemon.sweetsync.ui.theme.BpCrimson
import com.daemon.sweetsync.ui.theme.SugarOrange
import com.daemon.sweetsync.ui.theme.TextLight
import com.daemon.sweetsync.ui.theme.TextSecondary
import com.daemon.sweetsync.ui.theme.WeightTeal

@Composable
fun GlycemicSplineChart(
    modifier: Modifier = Modifier,
    height: Dp = 90.dp
) {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Today")
    // Values roughly normalized (0.0 to 1.0)
    val points = listOf(0.45f, 0.50f, 0.65f, 0.55f, 0.62f, 0.58f, 0.70f)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { day ->
                Text(
                    text = day,
                    fontSize = 11.sp,
                    fontWeight = if (day == "Today") FontWeight.Bold else FontWeight.Medium,
                    color = if (day == "Today") SugarOrange else TextSecondary
                )
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        ) {
            val w = size.width
            val h = size.height
            val step = w / (points.size - 1)

            val coords = points.mapIndexed { i, value ->
                val x = i * step
                val y = h - (value * (h * 0.75f) + h * 0.15f)
                Offset(x, y)
            }

            // Draw line path with smooth cubic bezier
            val strokePath = Path().apply {
                moveTo(coords[0].x, coords[0].y)
                for (i in 0 until coords.size - 1) {
                    val p0 = coords[i]
                    val p1 = coords[i + 1]
                    val cx = (p0.x + p1.x) / 2
                    cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                }
            }

            // Fill gradient path under curve
            val fillPath = Path().apply {
                addPath(strokePath)
                lineTo(coords.last().x, h)
                lineTo(coords.first().x, h)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SugarOrange.copy(alpha = 0.22f),
                        SugarOrange.copy(alpha = 0.02f)
                    ),
                    startY = 0f,
                    endY = h
                )
            )

            // Draw stroke
            drawPath(
                path = strokePath,
                color = SugarOrange,
                style = Stroke(
                    width = 3.5.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            // Draw points
            coords.forEachIndexed { index, pt ->
                drawCircle(
                    color = Color.White,
                    radius = 4.dp.toPx(),
                    center = pt
                )
                drawCircle(
                    color = SugarOrange,
                    radius = 3.dp.toPx(),
                    center = pt
                )
            }
        }
    }
}

@Composable
fun EcgPulseWave(
    modifier: Modifier = Modifier,
    color: Color = BpCrimson,
    strokeWidth: Dp = 2.dp,
    height: Dp = 24.dp
) {
    Canvas(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
    ) {
        val w = size.width
        val h = size.height
        val midY = h * 0.5f

        val path = Path().apply {
            moveTo(0f, midY)
            lineTo(w * 0.20f, midY)
            lineTo(w * 0.25f, midY - h * 0.15f)
            lineTo(w * 0.30f, midY + h * 0.15f)
            lineTo(w * 0.35f, midY)
            lineTo(w * 0.45f, midY)
            // Pulse spike
            lineTo(w * 0.50f, midY - h * 0.42f)
            lineTo(w * 0.55f, midY + h * 0.45f)
            lineTo(w * 0.60f, midY - h * 0.20f)
            lineTo(w * 0.65f, midY)
            lineTo(w * 0.75f, midY + h * 0.10f)
            lineTo(w * 0.82f, midY)
            lineTo(w, midY)
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Composable
fun MiniSugarWave(
    modifier: Modifier = Modifier,
    color: Color = SugarOrange
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(18.dp)
    ) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(0f, h * 0.6f)
            cubicTo(w * 0.25f, h * 0.1f, w * 0.65f, h * 0.9f, w, h * 0.3f)
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = 2.5.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}

@Composable
fun WeightTrendBars(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .width(5.dp)
                .height(10.dp)
                .background(WeightTeal.copy(alpha = 0.5f), RoundedCornerShape(2.dp))
        )
        Box(
            modifier = Modifier
                .width(5.dp)
                .height(14.dp)
                .background(WeightTeal.copy(alpha = 0.8f), RoundedCornerShape(2.dp))
        )
        Box(
            modifier = Modifier
                .width(5.dp)
                .height(18.dp)
                .background(WeightTeal, RoundedCornerShape(2.dp))
        )
    }
}
