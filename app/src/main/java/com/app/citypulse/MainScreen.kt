package com.app.citypulse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.app.citypulse.presentation.components.SearchTopbar
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.citypulse.navigation.NavGraph
import com.app.citypulse.data.enums.TipoCategoria
import com.app.citypulse.navigation.BottomNavBar
import com.app.citypulse.utils.bottomNavigationItemsList

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val authRoutes = listOf("login", "register", "register2")
    val showBottomBar = currentRoute !in authRoutes
    val showSearchTopBar = currentRoute == "map_screen" // Solo visible en MapScreen

    var selectedCategory by remember { mutableStateOf(TipoCategoria.NONE) }

    Scaffold(
        containerColor = Color.Transparent,

        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    items = bottomNavigationItemsList,
                    currentRoute = currentRoute
                ) { currentNavigationItem ->
                    navController.navigate(currentNavigationItem.route) {
                        navController.graph.startDestinationRoute?.let { startDestinationRoute ->
                            popUpTo(startDestinationRoute) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { innerPadding ->
        // Usamos un Box para superponer la barra encima del contenido
        Box(modifier = Modifier.fillMaxSize()) {
            // 1) Contenido principal (NavGraph) donde se muestra el mapa
            NavGraph(navController = navController, innerPadding = innerPadding)

            if (showSearchTopBar) {
                SearchTopbar(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(horizontal = 16.dp),
                    events = emptyList(), // Obtén los eventos desde un ViewModel compartido o usando un StateHolder compartido
                    selectedCategory = selectedCategory,
                    onCategorySelected = { newCategory -> selectedCategory = newCategory },
                    onEventSelected = { event ->
                        // Maneja esto con un ViewModel compartido o savedStateHandle
                    }
                )
            }
        }
    }
}