package com.app.citypulse.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.app.citypulse.navigation.Graph

@Composable
fun RootNavGraph(){
    val rootNavController = rememberNavController()
    NavHost(
        navController = rootNavController,
        route = Graph.RootGraph,
        startDestination = Graph.AuthGraph
    ){

    }
}