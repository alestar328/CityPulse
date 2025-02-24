package com.app.citypulse.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.citypulse.MainScreen
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.app.citypulse.presentation.screens.*
import com.app.citypulse.presentation.register_screens.RegisterScreen
import com.app.citypulse.presentation.register_screens.RegisterScreen2
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.presentation.viewmodel.FriendsViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    eventViewModel: EventViewModel,
    authViewModel: AuthViewModel,
    friendsViewModel: FriendsViewModel
) {
    val isAuthenticated = authViewModel.isAuthenticated.collectAsState().value
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "main_screen" else "login"
    ) {
        // Autenticación
        composable("login") {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }
        composable("register") {
            RegisterScreen(navController = navController, viewModel = authViewModel)
        }
        composable("register2") {
            RegisterScreen2(navController = navController, viewModel = authViewModel)
        }

        // Pantalla Principal
        composable("main_screen") {
            MainScreen(navController, authViewModel)
        }

        composable("friends") {
            // Usamos un estado para almacenar el usuario actual
            var currentUser by remember { mutableStateOf<UserItem?>(null) }

            // Ejecutamos la corutina al momento de la composición
            LaunchedEffect(Unit) {
                // Dentro de LaunchedEffect podemos llamar a funciones suspensivas
                currentUser = authViewModel.getCurrentUser() // Llamada suspensiva dentro de la corutina
            }

            // Llamamos a la pantalla de amigos solo cuando currentUser no sea null
            if (currentUser != null) {
                FriendsScreen(
                    navController = navController,
                    viewModel = authViewModel,
                    currentUser = currentUser!!
                )
            }
        }

        // Ruta para la pantalla de añadir amigo
        composable("addfriend") {
            AddFriendScreen(
                navController = navController,
                FriendsviewModel = friendsViewModel,
                AuthviewModel = authViewModel
            )
        }



        // Eventos
        composable("create_event") {
            CreateEventScreen(eventViewModel, navController)
        }
        composable("location_picker_screen") {
            LocationPickerScreen(navController)
        }
        composable("map_screen") {
            MapScreen(
                viewModel = eventViewModel,
                onLocationSelected = { latLng ->
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set("latitud", latLng.latitude)
                        set("longitud", latLng.longitude)
                    }
                    navController.popBackStack()
                },
                onMarkerClicked = { eventUi  ->
                    navController.navigate("event_details/${eventUi.id}")
                }
            )
        }

        // Detalles del evento
        composable("event_details/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            EventDetailsScreen(eventId = eventId, viewModel = eventViewModel, navController = navController)
        }

        // Otras pantallas
        composable("profile") {
            ProfileScreen(navController = navController, viewModel = authViewModel)
        }
        composable("settings") {
            SettingsScreen()
        }
    }
}
