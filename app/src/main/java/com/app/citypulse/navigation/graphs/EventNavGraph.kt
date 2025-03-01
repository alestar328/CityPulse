package com.app.citypulse.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.citypulse.navigation.AuthRouteScreen
import com.app.citypulse.navigation.EventRouteScreen
import com.app.citypulse.navigation.Graph

fun NavGraphBuilder.eventRouteScreen(rootNavController: NavHostController) {
    navigation(
        route = Graph.EventScreenGraph,
        startDestination = EventRouteScreen.ShowDetail.route
    ) {
        composable(route = EventRouteScreen.CreateEvent.route) {
        }
    }
}