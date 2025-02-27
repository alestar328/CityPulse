package com.app.citypulse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.citypulse.data.NavItem
import com.app.citypulse.presentation.components.SearchTopbar
import com.app.citypulse.presentation.screens.MapScreen
import com.app.citypulse.presentation.screens.SettingsScreen
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.citypulse.data.model.EventUiModel
import com.app.citypulse.data.repository.EventRepository
import com.app.citypulse.presentation.screens.ProfileScreen
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun MainScreen(navController: NavController = rememberNavController(), authViewModel: AuthViewModel) {

    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val uid = firebaseUser?.uid

    // Creamos instancia para manejar logica eventos en el mapa.
    val viewModel = EventViewModel(EventRepository())

    val navitemList = listOf(
        NavItem("Perfil", Icons.Default.Person, 5),
        NavItem("Mapa", Icons.Default.LocationOn, 0),
        NavItem("Config", Icons.Default.Settings, 0)
    )

    var selectedIndex by remember { mutableIntStateOf(1) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            NavigationBar {
                navitemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            BadgedBox(badge = {
                                if (navItem.badgeCount > 0)
                                    Badge { Text(text = navItem.badgeCount.toString()) }
                            }) {
                                Icon(imageVector = navItem.icon, contentDescription = "Icon")
                            }
                        },
                        label = { Text(text = navItem.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ContentScreen(
                modifier = Modifier.fillMaxSize(),
                selectedIndex = selectedIndex,
                navController = navController,
                viewModel = viewModel,
                authViewModel = authViewModel, // Se pasa el AuthViewModel aquí
                onMarkerClicked = { eventEntity ->
                    navController.navigate("event_details/${eventEntity.id}")
                }
            )
            if (selectedIndex == 1) {
                SearchTopbar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.TopCenter)
                        .background(Color.Transparent)
                )
            }
        }
    }
}


@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    navController: NavController,
    viewModel: EventViewModel,
    authViewModel: AuthViewModel,
    onMarkerClicked: (EventUiModel) -> Unit
) {
    when (selectedIndex) {
        0 -> ProfileScreen(navController = navController, viewModel = authViewModel)
        1 -> {
            MapScreen(
                viewModel = viewModel,
                onLocationSelected = { navController.navigate("create_event") },
                onMarkerClicked = onMarkerClicked,
                navController = navController,
                authViewModel = authViewModel
            )
        }
        2 -> SettingsScreen()
    }
}


