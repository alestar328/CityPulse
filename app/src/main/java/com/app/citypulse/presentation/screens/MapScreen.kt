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
import com.app.citypulse.data.enums.TipoCategoria
import com.app.citypulse.data.model.EventEntity
import com.app.citypulse.data.model.EventUiModel
import com.app.citypulse.presentation.components.EventOrganizerMapCard
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(viewModel: EventViewModel,
              selectedCategory: TipoCategoria = TipoCategoria.NONE,
              onLocationSelected: (LatLng) -> Unit,
              onMarkerClicked: (EventUiModel) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(41.57008436408339, 1.9954403499999671), 15f)
    }

    val eventLocations by viewModel.eventUiList.collectAsState()
  //  val markerStates = remember { mutableStateMapOf<String, MarkerState>() }
    var selectedEvent by remember { mutableStateOf<EventUiModel?>(null) }

    val filteredEvents = if (selectedCategory != TipoCategoria.NONE) {
        eventLocations.filter { event ->
            // Compara el string de la categoría (por ejemplo, "GASTRONOMICO") con el nombre del enum
            event.categoria.equals(selectedCategory.name, ignoreCase = true)
        }
    } else {
        eventLocations
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                // Cada vez que el usuario toque el mapa (y no un marcador):
                selectedEvent = null
            }
        ) {
            filteredEvents.forEach { event ->
                val position = LatLng(event.latitud, event.longitud)

                Marker(
                    state = rememberMarkerState(position = position),
                    title = event.nombre,
                    snippet = event.descripcion,
                    onClick = {
                        // Alterna la selección del evento
                        selectedEvent = if (selectedEvent == event) null else event
                        true // Indicamos que el evento se ha manejado
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

        // --- Tarjeta con la información del evento seleccionado ---
        selectedEvent?.let { event ->
            // Un contenedor para la tarjeta, alineado en la parte inferior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.BottomCenter)
                    // Opcional: puedes darle un fondo o un padding extra
                    .background(Color.White.copy(alpha = 0.9f))
                    .padding(8.dp)
            ) {
                // Aquí reutilizas tu EventOrganizerMapCard
                EventOrganizerMapCard(
                    nombre = event.nombre,
                    categoria = event.categoria,
                    subcategoria = event.subcategoria,
                    lugar = event.lugar,
                    fechaInicio = event.fechaInicio,
                    fechaFin = event.fechaFin,
                    precio = event.precio,
                    aforo = event.aforo
                )
            }
        }
    }
}

