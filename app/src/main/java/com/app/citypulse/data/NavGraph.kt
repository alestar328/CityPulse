package com.app.citypulse.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.citypulse.presentation.screens.*
import com.app.citypulse.presentation.viewmodel.AuthViewModel

@Composable
fun NavigationGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    val isAuthenticated = authViewModel.isAuthenticated.collectAsState().value

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "map" else "login"
    ) {
        composable("login") {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }
        composable("contacts") {
            ContactsScreen()
        }
        composable("map") {
            MapScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
    }
}
