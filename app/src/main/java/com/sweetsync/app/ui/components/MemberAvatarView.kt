package com.sweetsync.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sweetsync.app.R
import com.sweetsync.app.ui.theme.AppPrimary
import com.sweetsync.app.ui.theme.CardSurface
import com.sweetsync.app.ui.theme.StatusOptimalText

@Composable
fun MemberAvatarView(
    drawableName: String,
    initials: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    ringColor: Color? = null,
    showVerifiedBadge: Boolean = false,
    badgeColor: Color = AppPrimary
) {
    val context = LocalContext.current
    val resId = if (drawableName.isNotBlank()) {
        context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    } else 0

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        val imageModifier = Modifier
            .size(size)
            .then(
                if (ringColor != null) {
                    Modifier.border(2.dp, ringColor, CircleShape)
                } else Modifier
            )
            .clip(CircleShape)

        if (resId != 0) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = imageModifier.background(Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials.ifBlank { "M" },
                    fontSize = (size.value * 0.38f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569)
                )
            }
        }

        if (showVerifiedBadge) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .background(badgeColor, CircleShape)
                    .border(2.dp, CardSurface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Verified",
                    tint = Color.White,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
    }
}
