package com.app.citypulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.citypulse.presentation.screens.LoginScreen
import com.app.citypulse.presentation.screens.SettingsScreen
import com.app.citypulse.ui.theme.CityPulseTheme
import com.app.citypulse.ui.theme.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Splash Screen
        Thread.sleep(3000)
        installSplashScreen()

        setContent {
            CityPulseTheme {
                // Crea un NavController para gestionar la navegación
                val navController = rememberNavController()

                // Inicializa el ViewModel
                val authViewModel: AuthViewModel = viewModel()

                // Usamos NavHost para definir las pantallas y su navegación
                NavHost(navController = navController, startDestination = "login_screen") {
                    // Pantalla de Login
                    composable("login_screen") {
                        LoginScreen(navController = navController, viewModel = authViewModel)
                    }
                    // Pantalla de Settings
                    composable("settings_screen") {
                        SettingsScreen()
                    }
                }
            }
        }
    }
}
