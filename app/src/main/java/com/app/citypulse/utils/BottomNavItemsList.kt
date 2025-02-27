package com.app.citypulse.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import com.app.citypulse.navigation.NavItem
import com.app.citypulse.presentation.screens.ProfileScreen

val bottomNavigationItemsList = listOf(
    NavItem(
        title = "Perfil",
        badgeCount = 0,
        route = "profile",
        selectedIcon = Icons.Default.Person,
        unSelectedIcon = Icons.Default.Person
    ),
    NavItem(
        title = "Mapa",
        badgeCount = 0,
        route = "map_screen",
        selectedIcon = Icons.Default.LocationOn,
        unSelectedIcon = Icons.Default.LocationOn
    ),
    NavItem(
        title = "Config",
        badgeCount = 0,
        route = "settings",
        selectedIcon = Icons.Default.Settings,
        unSelectedIcon = Icons.Default.Settings
    )
)