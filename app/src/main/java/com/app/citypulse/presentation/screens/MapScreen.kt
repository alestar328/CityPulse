package com.app.citypulse.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.citypulse.data.model.EventEntity
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(viewModel: EventViewModel, onLocationSelected: (LatLng) -> Unit, onMarkerClicked: (EventEntity) -> Unit) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(41.57008436408339, 1.9954403499999671), 15f)
    }

    val eventLocations by viewModel.eventList.collectAsState()
    val markerStates = remember { mutableStateMapOf<String, MarkerState>() }
    var selectedEvent by remember { mutableStateOf<EventEntity?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            eventLocations.forEach { event ->
                val position = LatLng(event.latitud, event.longitud)
                val markerState = markerStates.getOrPut(event.id) { rememberMarkerState(position = position) }

                Marker(
                    state = markerState,
                    title = event.nombre,
                    snippet = event.descripcion,
                    onClick = {
                        selectedEvent = if (selectedEvent == event) null else event
                        true
                    }
                )
            }
        }

        // Solo muestra el botón correspondiente según el evento seleccionado
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, bottom = 18.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            FloatingActionButton(
                onClick = {
                    if (selectedEvent == null) {
                        // Navegar a la creación de eventos si no hay evento seleccionado
                        onLocationSelected(cameraPositionState.position.target)
                    } else {
                        // Navegar a los detalles del evento seleccionado
                        onMarkerClicked(selectedEvent!!)
                    }
                },
                modifier = Modifier.padding(4.dp),
                containerColor = if (selectedEvent == null) Color.LightGray else Color.Blue
            ) {
                if (selectedEvent == null) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Crear Evento")
                } else {
                    Text(text = "INFO", color = Color.White)
                }
            }
        }
    }
}

