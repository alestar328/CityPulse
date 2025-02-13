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

    // Lista de eventos en tiempo real.
    val eventLocations = viewModel.eventList

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ) {
            // Dibujar los eventos como marcadores.
            eventLocations.forEach { event ->
                Marker(
                    state = rememberMarkerState(position = LatLng(event.latitud, event.longitud)),
                    title = event.nombre,
                    snippet = event.descripcion
                )
            }
        }
    }
}




