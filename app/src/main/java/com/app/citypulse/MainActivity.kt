package com.app.citypulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.app.citypulse.data.NavigationGraph
import com.app.citypulse.data.repository.EventRepository
import com.app.citypulse.presentation.EventViewModel
import com.app.citypulse.presentation.screens.ui.theme.CityPulseTheme
import com.app.citypulse.presentation.viewmodel.AuthViewModel

import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        if (FirebaseApp.getApps(this).isEmpty()) {
            throw IllegalStateException("FirebaseApp no se ha inicializado correctamente.")
        }
        FirebaseFirestore.getInstance()

        enableEdgeToEdge()
        installSplashScreen()

        setContent {
            CityPulseTheme {
                val navController = rememberNavController()
                val eventRepository = EventRepository()
                val eventViewModel = EventViewModel(eventRepository)

                val authViewModel = AuthViewModel()

                NavigationGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    eventViewModel = eventViewModel
                )
                MainScreen(
                    authViewModel = authViewModel,
                    eventViewModel = eventViewModel
                )
            }
        }
    }
}
