package com.app.citypulse.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.citypulse.presentation.components.EventCard
import com.app.citypulse.presentation.ui.theme.TurkBlue
import com.app.citypulse.presentation.ui.theme.YellowLight
import com.app.citypulse.presentation.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedEventsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val savedEvents by userViewModel.savedEvents.collectAsState()
    Log.d("SavedEventsScreen", "savedEvents changed: $savedEvents")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Eventos guardados") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TurkBlue,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(savedEvents) { event ->
                    EventCard(
                        event    = event,
                        modifier = Modifier.padding(vertical = 8.dp),
                        onClick  = {navController.navigate("event_details/${event.id}")}
                    )
                }



        }
    }
}