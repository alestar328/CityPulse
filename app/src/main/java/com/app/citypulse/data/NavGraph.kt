package com.app.citypulse.data

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.citypulse.MainScreen
import com.app.citypulse.presentation.EventViewModel
import com.app.citypulse.presentation.screens.CreateEventScreen
import com.app.citypulse.presentation.screens.LocationPickerScreen

import com.app.citypulse.presentation.screens.MapScreen

@Composable
fun NavGraph(navController: NavHostController, viewModel: EventViewModel) {
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController)
        }
        composable("create_event") {
            CreateEventScreen(viewModel, navController)
        }
        composable("location_picker_screen") {
            LocationPickerScreen(navController)
        }
        composable("map_screen") {
            MapScreen(viewModel = viewModel, onLocationSelected = { latLng ->
                navController.previousBackStackEntry?.savedStateHandle?.apply {
                    set("latitud", latLng.latitude)
                    set("longitud", latLng.longitude)
                }
                navController.popBackStack()
            })
        }
    }
}
