package com.app.citypulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.app.citypulse.presentation.ui.theme.CityPulseTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        FirebaseFirestore.getInstance()

        //Splash Screen
        Thread.sleep(2000)
        installSplashScreen()

        setContent {
            CityPulseTheme {
                val systemUiController = rememberSystemUiController()

                // Aquí defines la barra de navegación en negro
                systemUiController.setNavigationBarColor(
                    color = Color.Black,
                    darkIcons = false // Íconos claros sobre fondo negro
                )

               
                MainScreen()

            }
        }
    }
}
