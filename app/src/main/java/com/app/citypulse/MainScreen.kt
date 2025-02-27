package com.app.citypulse

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.mutableStateOf
import com.app.citypulse.data.enums.TipoCategoria
import androidx.compose.runtime.derivedStateOf
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.citypulse.navigation.BottomNavBar
import com.app.citypulse.navigation.NavGraph
import com.app.citypulse.utils.bottomNavigationItemsList


@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.destination?.route
        }
    }
    val topBarTitle by remember(currentRoute) {
        derivedStateOf {
            if (currentRoute != null) {
                bottomNavigationItemsList[bottomNavigationItemsList.indexOfFirst {
                    it.route == currentRoute
                }].title
            } else {
                bottomNavigationItemsList[0].title
            }
        }
    }
    val authRoutes = listOf("login", "register", "register2")
    val showBottomBar = currentRoute !in authRoutes
    var selectedIndex by remember { mutableIntStateOf(1) }
    var selectedCategory by remember { mutableStateOf(TipoCategoria.NONE) }


    Scaffold(
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
        NavGraph(navController = navController, innerPadding = innerPadding)
    }
}