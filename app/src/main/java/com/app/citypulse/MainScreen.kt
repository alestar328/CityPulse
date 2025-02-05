package com.app.citypulse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.app.citypulse.data.NavItem
import com.app.citypulse.data.NavigationGraph
import com.app.citypulse.presentation.components.SearchTopbar
import com.app.citypulse.presentation.viewmodel.AuthViewModel

@Composable
fun MainScreen(authViewModel: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
// pongo un comentario para poder hacer un commit
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            if (isAuthenticated) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavigationGraph(navController, authViewModel) // Usa el NavigationGraph

            if (isAuthenticated) {
                SearchTopbar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.TopCenter)
                        .background(Color.Transparent)
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: androidx.navigation.NavController) {
    val navItemList = listOf(
        NavItem("contacts", Icons.Default.Person, 5),
        NavItem("map", Icons.Default.LocationOn, 0),
        NavItem("settings", Icons.Default.Settings, 0)
    )

    NavigationBar {
        navItemList.forEach { navItem ->
            NavigationBarItem(
                selected = navController.currentDestination?.route == navItem.label,
                onClick = { navController.navigate(navItem.label) },
                icon = {
                    BadgedBox(badge = {
                        if (navItem.badgeCount > 0)
                            Badge { Text(text = navItem.badgeCount.toString()) }
                    }) {
                        Icon(imageVector = navItem.icon, contentDescription = "Icon")
                    }
                },
                label = { Text(text = navItem.label) }
            )
        }
    }
}
