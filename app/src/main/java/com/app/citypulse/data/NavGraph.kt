package com.app.citypulse.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.citypulse.MainScreen
import com.app.citypulse.presentation.EventViewModel
import com.app.citypulse.presentation.screens.CreateEventScreen
import com.app.citypulse.presentation.screens.LocationPickerScreen

import com.app.citypulse.presentation.screens.MapScreen
import com.app.citypulse.presentation.register_screens.RegisterScreen
import com.app.citypulse.presentation.register_screens.RegisterScreen2
import com.app.citypulse.presentation.screens.*
import com.app.citypulse.presentation.viewmodel.AuthViewModel

@Composable
fun NavigationGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    val isAuthenticated = authViewModel.isAuthenticated.collectAsState().value
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "map" else "login"
    ) {
        composable("login") {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }
        composable("profile") {
            ProfileScreen(navController = navController, viewModel = authViewModel)
fun NavGraph(navController: NavHostController, viewModel: EventViewModel) {
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController)
        }
        composable("map") {
            MapScreen()
        composable("create_event") {
            CreateEventScreen(viewModel, navController)
        }
        composable("settings") {
            SettingsScreen()
        composable("location_picker_screen") {
            LocationPickerScreen(navController)
        }
        composable("register"){
            RegisterScreen(navController = navController, viewModel = authViewModel)
        }
        composable("register2"){
            RegisterScreen2(navController = navController, viewModel = authViewModel)
        composable("map_screen") {
            MapScreen(viewModel = viewModel, onLocationSelected = { latLng ->
                navController.previousBackStackEntry?.savedStateHandle?.apply {
                    set("latitud", latLng.latitude)
                    set("longitud", latLng.longitude)
                }
                navController.popBackStack()
            })
        }
        composable("assisted_events") {
            AssistedEventScreen(navController = navController)
        }
        composable("saved_events") {
            SavedEventScreen(navController = navController)
        }

    }
}
