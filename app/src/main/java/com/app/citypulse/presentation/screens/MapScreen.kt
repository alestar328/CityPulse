package com.app.citypulse.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(viewModel: EventViewModel, onLocationSelected: (LatLng) -> Unit) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(41.387054, 2.170210), 12f)
    }

    val eventLocations by viewModel.eventList.collectAsState()

    val markerStates = remember { mutableStateMapOf<String, MarkerState>() } // 🔹 Map para manejar estados de los marcadores

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // 🔹 Limpiar el mapa antes de añadir nuevos marcadores
            markerStates.clear()

            eventLocations.forEach { event ->
                val position = LatLng(event.latitud, event.longitud)

                // 🔹 Crear o actualizar el estado del marcador
                val markerState = markerStates.getOrPut(event.id) { rememberMarkerState(position = position) }
                markerState.position = position

                Marker(
                    state = markerState,
                    title = event.nombre,
                    snippet = event.descripcion
                )
            }
        }
    }
}
