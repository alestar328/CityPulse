package com.app.citypulse.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.citypulse.presentation.viewmodel.EventViewModel

@Composable
fun EventDetailsScreen(eventId: String, viewModel: EventViewModel) {
    val event by viewModel.getEventById(eventId).collectAsState(initial = null)

    event?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nombre: ${it.nombre}")
            Text("Categoría: ${it.categoria.displayName}")
            Text("Descripción: ${it.descripcion}")
            Text("Ubicación: ${it.lugar}")
            Text("Fecha y hora de inicio: ${it.fechaInicio}")
            Text("Fecha y hora de fin: ${it.fechaFin}")
            Text("Precio: ${it.precio}")
            Text("Aforo: ${it.aforo}")
        }
    } ?: Text("Cargando detalles del evento...")
}
