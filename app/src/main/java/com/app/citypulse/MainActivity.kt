package com.app.citypulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.app.citypulse.data.repository.EventRepository
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.presentation.screens.ui.theme.CityPulseTheme
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

                val navController = rememberNavController()
                val eventRepository = EventRepository()
                val eventViewModel = EventViewModel(eventRepository)
                val authViewModel = AuthViewModel()

                NavigationGraph(navController = navController, eventViewModel = eventViewModel, authViewModel = authViewModel)
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            // Oculta la barra de navegación (botones virtuales) y la barra de estado si lo deseas
            hide(WindowInsetsCompat.Type.navigationBars())

            // Comportamiento: se muestran transitoriamente al deslizar desde el borde
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

    }
}
