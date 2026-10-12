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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daemon.sweetsync.ui.theme.AppPrimary
import com.daemon.sweetsync.ui.theme.CardStroke
import com.daemon.sweetsync.ui.theme.CardSurface
import com.daemon.sweetsync.ui.theme.TextLight
import com.daemon.sweetsync.ui.theme.TextSecondary

sealed class NavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : NavItem("home", "Home", Icons.Outlined.Home)
    object AddMember : NavItem("add_member", "Add Member", Icons.Outlined.PersonAdd)
    object Records : NavItem("records_menu", "Records", Icons.AutoMirrored.Outlined.ShowChart)
    object Account : NavItem("account", "Account", Icons.Outlined.Settings)
}

@Composable
fun SweetSyncBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem.Home,
        NavItem.AddMember,
        NavItem.Records,
        NavItem.Account
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .background(CardSurface)
            .border(width = 1.dp, color = CardStroke)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = when (item) {
                is NavItem.Home -> currentRoute == "home"
                is NavItem.AddMember -> currentRoute == "add_member"
                is NavItem.Records -> currentRoute.startsWith("blood_sugar") || currentRoute.startsWith("blood_pressure") || currentRoute.startsWith("weight") || currentRoute == "records_menu"
                is NavItem.Account -> currentRoute == "account"
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigate(item.route) }
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (isSelected) AppPrimary else TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.label,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) AppPrimary else TextSecondary
                )
            }
        }
    }
}
