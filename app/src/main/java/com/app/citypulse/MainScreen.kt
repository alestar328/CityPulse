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
fun MainScreen(authViewModel: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    // 1) Aqui empieza la logica para Topbar solo se vea en Map
    val currentBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(initial = null)
    val currentRoute = currentBackStackEntry?.destination?.route

    val viewModel = EventViewModel(com.app.citypulse.data.repository.EventRepository()) // Crear el ViewModel

    val navitemList = listOf(
        NavItem("Contacts", Icons.Default.Person, 5),
        NavItem("Map", Icons.Default.LocationOn, 0),
        NavItem("Settings", Icons.Default.Settings, 0)
    )

    var selectedIndex by remember {
        mutableIntStateOf(1)
    }

    //Este Scaffold ha sido traido desde el MainActivity
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent, // Evita que Scaffold agregue un fondo que ocupe espacio
        contentWindowInsets = WindowInsets(0.dp), // Elimina cualquier margen superior

        bottomBar = {
            if (isAuthenticated) {
                BottomNavigationBar(navController)
            }

        }
    ) { innerPadding ->
        // Contenido de las pantallas
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Asegura que el contenido no se superponga con el topBar
        ) {
            NavigationGraph(navController, authViewModel) // Usa el NavigationGraph

            if (isAuthenticated && currentRoute == "map") {
                SearchTopbar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp)
                        .align(Alignment.TopCenter)
                        .background(Color.Transparent)
                )
            }
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
fun BottomNavigationBar(navController: androidx.navigation.NavController) {
    val navItemList = listOf(
        NavItem("profile", Icons.Default.Person, 5),
        NavItem("map", Icons.Default.LocationOn, 0),
        NavItem("settings", Icons.Default.Settings, 0)
    )

    NavigationBar(
        containerColor = TurkBlue
    ) {
        navItemList.forEach { navItem ->
            NavigationBarItem(
                selected = navController.currentDestination?.route == navItem.label,
                onClick = { navController.navigate(navItem.label) },
                icon = {
                    BadgedBox(badge = {
                        if (navItem.badgeCount > 0)
                            Badge { Text(text = navItem.badgeCount.toString()) }
                    }) {
                        Icon(imageVector = navItem.icon, contentDescription = "Icon", tint = Color.White)
                    }
                },
                label = { Text(text = navItem.label) }
            )
        }
    }

}