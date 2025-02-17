package com.app.citypulse.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.citypulse.MainScreen
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.app.citypulse.presentation.screens.*
import com.app.citypulse.presentation.register_screens.RegisterScreen
import com.app.citypulse.presentation.register_screens.RegisterScreen2
import com.app.citypulse.presentation.viewmodel.AuthViewModel

@Composable
fun NavigationGraph(navController: NavHostController, eventViewModel: EventViewModel, authViewModel: AuthViewModel) {
    val isAuthenticated = authViewModel.isAuthenticated.collectAsState().value
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "main_screen" else "login"
    ) {
        // Autenticación
        composable("login") {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }
        composable("register") {
            RegisterScreen(navController = navController, viewModel = authViewModel)
        }
        composable("register2") {
            RegisterScreen2(navController = navController, viewModel = authViewModel)
        }

        // Pantalla Principal
        composable("main_screen") {
            MainScreen(navController, authViewModel)
        }

        // Eventos
        composable("create_event") {
            CreateEventScreen(eventViewModel, navController)
        }
        composable("location_picker_screen") {
            LocationPickerScreen(navController)
        }
        composable("map_screen") {
            MapScreen(viewModel = eventViewModel, onLocationSelected = { latLng ->
                navController.previousBackStackEntry?.savedStateHandle?.apply {
                    set("latitud", latLng.latitude)
                    set("longitud", latLng.longitude)
                }
                navController.popBackStack()
            })
        }

        // Otras pantallas
        composable("contacts") {
            ContactsScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
    }
}
