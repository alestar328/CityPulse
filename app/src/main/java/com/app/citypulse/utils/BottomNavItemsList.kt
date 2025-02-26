package com.app.citypulse.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import com.app.citypulse.navigation.NavItem
import com.app.citypulse.presentation.screens.ProfileScreen

val bottomNavigationItemsList = listOf(
    NavItem("Perfil", Icons.Default.Person, 0, route = screens.ProfileScreen.route),
    NavItem("Mapa", Icons.Default.LocationOn, 0),
    NavItem("Config", Icons.Default.Settings, 0)
)