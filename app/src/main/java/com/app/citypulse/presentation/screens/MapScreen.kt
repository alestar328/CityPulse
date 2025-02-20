package com.app.citypulse.presentation.screens

import android.util.Log
import  androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.citypulse.data.model.EventUiModel
import com.app.citypulse.presentation.components.EventOrganizerMapCard
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(
    viewModel: EventViewModel,
    onLocationSelected: (LatLng) -> Unit,
    onMarkerClicked: (EventUiModel) -> Unit,
    navController: NavController,
    authViewModel: AuthViewModel // 🔥 Agregamos el AuthViewModel
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(41.57008436408339, 1.9954403499999671), 15f)
    }

    val eventLocations by viewModel.eventUiList.collectAsState()
    val markerStates = remember { mutableStateMapOf<String, MarkerState>() }
    var selectedEvent by remember { mutableStateOf<EventUiModel?>(null) }

    // 🔥 Obtenemos el tipo de usuario
    val userType by authViewModel.userType.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                selectedEvent = null
            }
        ) {
            eventLocations.forEach { event ->
                val position = LatLng(event.latitud, event.longitud)
                val markerState = markerStates.getOrPut(event.id) {
                    rememberMarkerState(position = position)
                }
                Marker(
                    state = markerState,
                    onClick = {
                        selectedEvent = if (selectedEvent == event) null else event
                        true
                    }
                )
            }
        }

        // 🔥 Mostrar el botón solo si el usuario es Organizador
        if (userType == "Organizador" || userType == "Asociacion") {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, bottom = 18.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                FloatingActionButton(
                    onClick = {
                        if (selectedEvent == null) {
                            onLocationSelected(cameraPositionState.position.target)
                        } else {
                            onMarkerClicked(selectedEvent!!)
                        }
                    },
                    modifier = Modifier.padding(4.dp),
                    containerColor = if (selectedEvent == null) Color.LightGray else Color.Blue
                ) {
                    if (selectedEvent == null) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Crear Evento")
                    }
                }
            }
        }

        // Tarjeta con la información del evento seleccionado
        selectedEvent?.let { event ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.BottomCenter)
                    .background(Color.White.copy(alpha = 0.9f))
                    .padding(8.dp)
            ) {
                EventOrganizerMapCard(
                    nombre = event.nombre,
                    categoria = event.categoria,
                    subcategoria = event.subcategoria,
                    lugar = event.lugar,
                    fechaInicio = event.fechaInicio,
                    fechaFin = event.fechaFin,
                    precio = event.precio,
                    aforo = event.aforo,
                    eventId = event.id,
                    navController = navController
                )
            }
        }
    }
}


