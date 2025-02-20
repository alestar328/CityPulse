package com.app.citypulse.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.citypulse.presentation.viewmodel.EventViewModel

@Composable
fun EventDetailsScreen(
    eventId: String,
    viewModel: EventViewModel,
    navController: NavController
) {
    // Se dispara la carga de los detalles usando el eventId
    LaunchedEffect(eventId) {
        viewModel.getEventById(eventId)
    }

    // Supongamos que en el ViewModel has adaptado eventDetails para que sea un EventUiModel
    val event = viewModel.eventDetails.value

    event?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nombre: ${it.nombre}")
            Text("Categoría: ${it.categoria}")
            Text("Descripción: ${it.descripcion}")
            Text("Ubicación: ${it.lugar}")
            Text("Fecha y hora de inicio: ${it.fechaInicio}")  // Formateada previamente
            Text("Fecha y hora de fin: ${it.fechaFin}")        // Formateada previamente
            Text("Precio: ${it.precio}")
            Text("Aforo: ${it.aforo}")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.popBackStack() }) {
                Text("Volver")
            }
        }
    } ?: Text("Cargando detalles del evento...")
}