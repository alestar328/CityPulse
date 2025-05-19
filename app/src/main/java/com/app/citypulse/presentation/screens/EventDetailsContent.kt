package com.app.citypulse.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.citypulse.presentation.viewmodel.EventDetailsUiState
import com.app.citypulse.presentation.viewmodel.EventViewModel

@Composable
fun EventDetailsContent(
    eventId: String,
    viewModel: EventViewModel = hiltViewModel(),
    navController: NavController,
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {
    // 1️⃣ Estado del ViewModel
    val uiState by viewModel.eventDetailsUiState.collectAsState()

    // 2️⃣ Disparar la carga al entrar en pantalla
    LaunchedEffect(eventId) {
        viewModel.getEventById(eventId)
    }

    // 3️⃣ Render según el estado
    when (uiState) {
        is EventDetailsUiState.Loading -> {
            // Indicador de carga centrado
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is EventDetailsUiState.Error -> {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Error al cargar el evento. Intenta de nuevo.")
            }
        }

        is EventDetailsUiState.NotFound -> {
            // TODO: UI para “no encontrado”
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Evento no encontrado.")
            }
        }

        is EventDetailsUiState.Success -> {
            // Extraemos los datos limpios del estado
            val success = uiState as EventDetailsUiState.Success
            val event = success.event
            val isCreator = success.isCreator

            // Llamada al componente UI “tonto”, sólo datos + callbacks
            EventDetailsScreen(event = event, navController = navController)

        }
    }
}