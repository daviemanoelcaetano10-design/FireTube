package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RetroCardBorder
import com.example.ui.theme.RetroDarkText
import com.example.ui.theme.RetroHyperlinkBlue
import com.example.ui.theme.RetroMutedGray

enum class RetroNavTab {
    HOME,
    EXPLORE,
    COMMUNITY
}

@Composable
fun RetroBottomNav(
    currentTab: RetroNavTab,
    onTabSelected: (RetroNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(width = 1.dp, color = RetroCardBorder)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Tab
            RetroNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = currentTab == RetroNavTab.HOME,
                testTag = "nav_tab_home",
                onClick = { onTabSelected(RetroNavTab.HOME) }
            )

            // Explore Tab
            RetroNavItem(
                icon = Icons.Default.Explore,
                label = "Explore",
                isSelected = currentTab == RetroNavTab.EXPLORE,
                testTag = "nav_tab_explore",
                onClick = { onTabSelected(RetroNavTab.EXPLORE) }
            )

            // Community Tab
            RetroNavItem(
                icon = Icons.Default.Group,
                label = "Community",
                isSelected = currentTab == RetroNavTab.COMMUNITY,
                testTag = "nav_tab_community",
                onClick = { onTabSelected(RetroNavTab.COMMUNITY) }
            )
        }
    }
}

@Composable
private fun RetroNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) RetroHyperlinkBlue else RetroMutedGray
    val containerBg = if (isSelected) Color(0xFFE8EEF8) else Color.Transparent

    Box(
        modifier = Modifier
            .testTag(testTag)
            .defaultMinSize(minWidth = 72.dp, minHeight = 48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(containerBg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Color(0x220033CC)),
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(19.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 12.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = contentColor
            )
        }
    }
}
