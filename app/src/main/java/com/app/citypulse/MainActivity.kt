package com.app.citypulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.app.citypulse.presentation.ui.theme.CityPulseTheme
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
                MainScreen()

            }
        }
    }
}
