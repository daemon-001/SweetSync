package com.sweetsync.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sweetsync.app.ui.theme.PillShape
import com.sweetsync.app.ui.theme.StatusNormalBg
import com.sweetsync.app.ui.theme.StatusNormalText
import com.sweetsync.app.ui.theme.StatusOptimalBg
import com.sweetsync.app.ui.theme.StatusOptimalText
import com.sweetsync.app.ui.theme.StatusWarningBg
import com.sweetsync.app.ui.theme.StatusWarningText

@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color? = null,
    backgroundColor: Color? = null
) {
    val (finalTextColor, finalBgColor) = when {
        textColor != null && backgroundColor != null -> Pair(textColor, backgroundColor)
        text.contains("Optimal", ignoreCase = true) || text.contains("Target", ignoreCase = true) || text.contains("Verified", ignoreCase = true) || text.contains("Healthy", ignoreCase = true) ->
            Pair(StatusOptimalText, StatusOptimalBg)
        text.contains("Normal", ignoreCase = true) || text.contains("Fasting", ignoreCase = true) || text.contains("Athlete", ignoreCase = true) || text.contains("Tracked", ignoreCase = true) || text.contains("In Motion", ignoreCase = true) ->
            Pair(StatusNormalText, StatusNormalBg)
        text.contains("Warning", ignoreCase = true) || text.contains("High", ignoreCase = true) ->
            Pair(StatusWarningText, StatusWarningBg)
        else ->
            Pair(StatusNormalText, StatusNormalBg)
    }

    Box(
        modifier = modifier
            .background(color = finalBgColor, shape = PillShape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = finalTextColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
