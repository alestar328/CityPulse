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
import com.app.citypulse.navigation.NavigationGraph
import com.app.citypulse.presentation.viewmodel.FriendsViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

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
                val eventViewModel = EventViewModel(eventRepository)
                val authViewModel = AuthViewModel()
                val friendsViewModel = FriendsViewModel(authViewModel)

                NavigationGraph(navController = navController, eventViewModel = eventViewModel,
                    authViewModel = authViewModel, friendsViewModel = FriendsViewModel(authViewModel)
                )
            }
        }
  /*      WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            // Oculta la barra de navegación (botones virtuales) y la barra de estado si lo deseas
            hide(WindowInsetsCompat.Type.navigationBars())
            // Si también quieres ocultar la status bar, usa:
            // hide(WindowInsetsCompat.Type.systemBars())

            // Comportamiento: se muestran transitoriamente al deslizar desde el borde
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
*/
    }
}
