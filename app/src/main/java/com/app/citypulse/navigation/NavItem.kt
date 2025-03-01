package com.app.citypulse.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val title : String,
    val badgeCount : Int? = null,
    val route : String,
    val selectedIcon : ImageVector,
    val unSelectedIcon : ImageVector,
    val hasBadgeDot: Boolean = false
)
