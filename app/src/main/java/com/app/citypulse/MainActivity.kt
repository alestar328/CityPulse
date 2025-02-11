package com.app.citypulse

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.app.citypulse.data.repository.EventRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph
import com.app.citypulse.presentation.EventViewModel
import com.app.citypulse.presentation.screens.ui.theme.CityPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        FirebaseFirestore.getInstance()

        enableEdgeToEdge()

        //Splash Screen
        Thread.sleep(2000)
        installSplashScreen()

        setContent {
            CityPulseTheme {
                val navController = rememberNavController()
                val eventRepository = EventRepository()
                val viewModel = EventViewModel(eventRepository)

                // Usamos NavGraph en lugar de definir el NavHost directamente aquí
                NavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}

