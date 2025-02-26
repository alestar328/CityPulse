package com.app.citypulse.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.citypulse.navigation.AuthRouteScreen
import com.app.citypulse.navigation.Graph
import com.app.citypulse.navigation.ProfileRouteScreen

fun NavGraphBuilder.authNavGraph(rootNavController: NavHostController) {
    navigation(
        route = Graph.AuthGraph,
        startDestination = AuthRouteScreen.Login.route

    ) {
        composable(route = AuthRouteScreen.Login.route) {
        }
        composable(route = AuthRouteScreen.Register.route) {
        }
        composable(route = AuthRouteScreen.Register2.route) {
        }
    }
}