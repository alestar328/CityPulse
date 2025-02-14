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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.filled.Add
import com.app.citypulse.data.repository.EventRepository
import com.app.citypulse.presentation.screens.ProfileScreen
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.presentation.viewmodel.EventViewModel
import java.util.Locale

@Composable
fun MainScreen(navController: NavController = rememberNavController(), authViewModel: AuthViewModel) {

    // Creamos instancia para manejar logica eventos en el mapa.
    val eventViewModel = EventViewModel(EventRepository())

    val navitemList = listOf(
        NavItem("Profile", Icons.Default.Person, 5),
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
                        onClick = {
                            selectedIndex = index
                            navController.navigate(navitemList[index].label.lowercase(Locale.getDefault()))
                        },
                        icon = { Icon(imageVector = navItem.icon, contentDescription = navItem.label) },
                        label = { Text(text = navItem.label) }
                    )
                }
            }
        },
        floatingActionButton = {
            if (selectedIndex == 1) {
                FloatingActionButton(onClick = { navController.navigate("create_event") }, containerColor = Color.LightGray) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Crear Evento")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            ContentScreen(selectedIndex, navController, eventViewModel, authViewModel)
            if (selectedIndex == 1) {
                SearchTopbar(modifier = Modifier.fillMaxWidth().padding(16.dp).align(Alignment.TopCenter).background(Color.Transparent))
            }
        }
    }
}



@Composable
fun ContentScreen(selectedIndex: Int, navController: NavController, eventViewModel: EventViewModel, authViewModel: AuthViewModel) {
    when (selectedIndex) {
        0 -> ProfileScreen(navController = navController, viewModel = authViewModel)
        1 -> MapScreen(viewModel = eventViewModel, onLocationSelected = { latLng ->
            navController.previousBackStackEntry?.savedStateHandle?.apply {
                set("latitud", latLng.latitude)
                set("longitud", latLng.longitude)
            }
            navController.popBackStack()
        })
        2 -> SettingsScreen()
    }
}

