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
import com.app.citypulse.presentation.screens.ContactsScreen
import com.app.citypulse.presentation.screens.MapScreen
import com.app.citypulse.presentation.screens.SettingsScreen
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.filled.Add
import com.app.citypulse.presentation.EventViewModel

@Composable
fun MainScreen(navController: NavController = rememberNavController()) {

    val viewModel = EventViewModel(com.app.citypulse.data.repository.EventRepository()) // Crear el ViewModel

    val navitemList = listOf(
        NavItem("Contacts", Icons.Default.Person, 5),
        NavItem("Map", Icons.Default.LocationOn, 0),
        NavItem("Settings", Icons.Default.Settings, 0)
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
        },
        floatingActionButton = {
            if (selectedIndex == 1) { // Solo mostrar el botón en la pantalla del mapa
                FloatingActionButton(
                    onClick = { navController.navigate("create_event") },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Crear Evento")
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
                viewModel = viewModel
            )
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


@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    navController: NavController,
    viewModel: EventViewModel
) {
    when (selectedIndex) {
        0 -> ContactsScreen()
        1 -> {
            // Agregar el parámetro onLocationSelected aquí también
            MapScreen(
                viewModel = viewModel,
                onLocationSelected = { latLng ->
                    // Acción a tomar cuando se selecciona una ubicación
                    navController.previousBackStackEntry?.savedStateHandle?.apply {
                        set("latitud", latLng.latitude)
                        set("longitud", latLng.longitude)
                    }
                    navController.popBackStack() // Volver atrás después de seleccionar la ubicación
                }
            )
        }
        2 -> SettingsScreen()
    }
}


