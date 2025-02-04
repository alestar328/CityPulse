package com.app.citypulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.citypulse.data.repository.EventRepository
import com.app.citypulse.presentation.EventViewModel
import com.app.citypulse.presentation.screens.CreateEventScreen
import com.app.citypulse.presentation.ui.theme.CityPulseTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase.
        FirebaseApp.initializeApp(this)

        // Inicializar Firestore.
        val firestore = FirebaseFirestore.getInstance()


        // Habilitar pantalla de borde a borde
        enableEdgeToEdge()

        // Configuración del SplashScreen
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                Thread.sleep(2000)
                false
            }
        }

        setContent {
            CityPulseTheme {
                val navController = rememberNavController()
                val eventRepository = EventRepository() // Crear el repositorio manualmente
                val viewModel = EventViewModel(eventRepository) // Pasar el repositorio al ViewModel

                NavHost(navController = navController, startDestination = "main_screen") {
                    composable("main_screen") { MainScreen(navController) }
                    composable("create_event") { CreateEventScreen(viewModel, navController) }
                }
            }
        }
    }
}
