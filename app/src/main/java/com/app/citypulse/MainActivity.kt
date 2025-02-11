package com.app.citypulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.app.citypulse.data.repository.EventRepository
import com.app.citypulse.presentation.EventViewModel
import com.app.citypulse.presentation.ui.theme.CityPulseTheme
import com.app.citypulse.data.NavigationGraph
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
                // Maneja navegación entre pantallas.
                val navController = rememberNavController()
                val eventRepository = EventRepository()
                // Maneja logica de eventos.
                val viewModel = EventViewModel(eventRepository)

                NavigationGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}
