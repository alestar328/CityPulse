package com.app.citypulse

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.app.citypulse.data.NavigationGraph
import com.app.citypulse.data.repository.EventRepository
import com.app.citypulse.presentation.EventViewModel
import com.app.citypulse.presentation.screens.ui.theme.CityPulseTheme
import com.app.citypulse.presentation.ui.theme.CityPulseTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

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
                // Maneja navegación entre pantallas.
                val navController = rememberNavController()
                val eventRepository = EventRepository()
                // Maneja logica de eventos.
                val viewModel = EventViewModel(eventRepository)

                NavigationGraph(navController = navController, viewModel = viewModel)
                MainScreen()
            }
        }
    }
}

