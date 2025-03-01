package com.app.citypulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.app.citypulse.data.repository.EventRepository
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.presentation.screens.ui.theme.CityPulseTheme
import com.app.citypulse.navigation.NavGraph
import com.app.citypulse.presentation.viewmodel.FriendsViewModel
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
