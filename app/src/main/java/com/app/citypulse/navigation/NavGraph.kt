package com.app.citypulse.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.citypulse.data.dataUsers.UserItem
import com.app.citypulse.data.enums.TipoCategoria
import com.app.citypulse.data.model.EventUiModel
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.app.citypulse.presentation.screens.*
import com.app.citypulse.presentation.register_screens.RegisterScreen
import com.app.citypulse.presentation.register_screens.RegisterScreen2
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.presentation.viewmodel.FriendsViewModel
import com.app.citypulse.presentation.viewmodel.LocationViewModel
import com.app.citypulse.presentation.viewmodel.SettingsViewModel
import com.app.citypulse.presentation.viewmodel.UserViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    locationViewModel: LocationViewModel,
    innerPadding: PaddingValues,
    eventLocations: List<EventUiModel>,
    selectedCategory: TipoCategoria
) {
    val eventViewModel: EventViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val friendsViewModel: FriendsViewModel = viewModel(factory = FriendsViewModel.FriendsViewModelFactory(authViewModel))

    val isAuthenticated = authViewModel.isAuthenticated.collectAsState().value

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "map_screen" else "login"
    ) {
        // Autenticación
        composable("login") {
            LoginScreen(navController = navController, viewModel = authViewModel,innerPadding = innerPadding)
        }
        composable("register") {
            RegisterScreen(navController = navController, viewModel = authViewModel, innerPadding = innerPadding)
        }
        composable("register2") {
            RegisterScreen2(navController = navController, viewModel = authViewModel, innerPadding = innerPadding)
        }

        composable("language_screen") {
            LanguageScreen(navController)
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
                    viewModel = friendsViewModel,
                    innerPadding = PaddingValues()
                )
            }
        }

        // Ruta para la pantalla de añadir amigo
        composable("addfriend") {
            AddFriendScreen(
                navController = navController,
                friendsViewModel = friendsViewModel,
                authViewModel = authViewModel,
                innerPadding = innerPadding,
            )
        }

        // Eventos
        composable("create_event") {
            CreateEventScreen(eventViewModel, authViewModel,navController, innerPadding, settingsViewModel)
        }

        composable("location_picker_screen") {
            LocationPickerScreen(navController, innerPadding = innerPadding)
        }

        composable("map_screen") { backStackEntry ->
            val searchEventId: String? =
                backStackEntry.savedStateHandle
                    .get<String>("searchEventId")
                    .also { /* opcional: limpiar si no la quieres persistente */ }
            MapScreen(
                viewModel = eventViewModel,
                userViewModel = userViewModel,
                authViewModel   = authViewModel,
                locationViewModel = locationViewModel,
                selectedCategory = selectedCategory,
                eventLocations     = eventLocations,
                onLocationSelected  = { latLng ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("latitud", latLng.latitude)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("longitud", latLng.longitude)
                    navController.popBackStack()
                },
                onMarkerClicked = { eventEntity ->
                    navController.navigate("event_details/${eventEntity.id}")
                },
                navController = navController,
                innerPadding = innerPadding,
                searchEventId     = searchEventId
            )
        }

        // Detalles del evento
        composable("event_details/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            EventDetailsContent(eventId = eventId, viewModel = eventViewModel, navController = navController, innerPadding = innerPadding)
        }

        composable("edit_event/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            EditEventScreen(eventId = eventId, viewModel = eventViewModel, navController = navController)
        }

        // Otras pantallas
        composable("profile") {
            ProfileScreen(navController = navController, viewModel = authViewModel, innerPadding = innerPadding)
        }
        composable("settings") {
            SettingsScreen( navController, innerPadding = innerPadding)
        }
        // Pantalla de ForgotPass
        composable("ForgotPass") {
            ContraseñaOlvidada(navController = navController)
        }
        composable("saved_events") {
            SavedEventsScreen(navController = navController, userViewModel = userViewModel)
        }

        composable("assisted_events") {
            AssistedEventsScreen(navController = navController,userViewModel = userViewModel)
        }
    }
}
