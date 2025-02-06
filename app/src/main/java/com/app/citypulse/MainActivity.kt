package com.app.citypulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.citypulse.data.repository.EventRepository
import com.app.citypulse.presentation.EventViewModel
import com.app.citypulse.presentation.screens.CreateEventScreen
import com.app.citypulse.presentation.screens.LocationPickerScreen
import com.app.citypulse.presentation.screens.MapScreen
import com.app.citypulse.presentation.ui.theme.CityPulseTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        FirebaseFirestore.getInstance()

        enableEdgeToEdge()

        setContent {
            CityPulseTheme {
                val navController = rememberNavController()
                val eventRepository = EventRepository()
                val viewModel = EventViewModel(eventRepository)

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
        }
    }
}
