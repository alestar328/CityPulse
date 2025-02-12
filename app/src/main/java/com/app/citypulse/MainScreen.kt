package com.app.citypulse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.app.citypulse.data.NavItem
import com.app.citypulse.data.NavigationGraph
import com.app.citypulse.presentation.EventViewModel
import com.app.citypulse.presentation.components.SearchTopbar
import com.app.citypulse.presentation.screens.MapScreen
import com.app.citypulse.presentation.screens.ui.theme.TurkBlue
import com.app.citypulse.presentation.viewmodel.AuthViewModel

@Composable
fun MainScreen(
    authViewModel: AuthViewModel = viewModel(),
    eventViewModel: EventViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val currentBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(initial = null)
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            if (isAuthenticated) {
                BottomNavigationBar(navController)
            }
        },
        floatingActionButton = {
            if (isAuthenticated && currentRoute == "map_screen") {
                FloatingActionButton(
                    onClick = { navController.navigate("create_event") },
                    modifier = Modifier.padding(16.dp),
                    containerColor = Color.LightGray
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Crear Evento")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavigationGraph(navController, authViewModel, eventViewModel)

            if (isAuthenticated && currentRoute == "map_screen") {
                SearchTopbar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp)
                        .align(Alignment.TopCenter)
                        .background(Color.Transparent)
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navItemList = listOf(
        NavItem("profile", Icons.Default.Person, 5),
        NavItem("map_screen", Icons.Default.LocationOn, 0),
        NavItem("settings", Icons.Default.Settings, 0)
    )

    NavigationBar(containerColor = TurkBlue) {
        navItemList.forEach { navItem ->
            NavigationBarItem(
                selected = navController.currentDestination?.route == navItem.label,
                onClick = { navController.navigate(navItem.label) },
                icon = {
                    BadgedBox(badge = {
                        if (navItem.badgeCount > 0)
                            Badge { Text(text = navItem.badgeCount.toString()) }
                    }) {
                        Icon(imageVector = navItem.icon, contentDescription = "Icon", tint = Color.White)
                    }
                },
                label = { Text(text = navItem.label) }
            )
        }
    }
}
