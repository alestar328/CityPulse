package com.app.citypulse.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
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
import com.app.citypulse.data.enums.TipoCategoria
import com.app.citypulse.presentation.components.SearchTopbar
import com.app.citypulse.presentation.ui.theme.TurkBlue

@Composable
fun MapScreen(
    viewModel: EventViewModel,
    onLocationSelected: (LatLng) -> Unit,
    onMarkerClicked: (EventUiModel) -> Unit,
    navController: NavController,
    authViewModel: AuthViewModel,
    selectedCategory: TipoCategoria,
    innerPadding: PaddingValues
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(41.57008436408339, 1.9954403499999671), 15f)
    }

    val eventLocations by viewModel.eventUiList.collectAsState()
    val markerStates = remember { mutableStateMapOf<String, MarkerState>() }
    var selectedEvent by remember { mutableStateOf<EventUiModel?>(null) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) } //coge ubicacion al presionar dedo
    var currentCategory by remember { mutableStateOf(selectedCategory) }

    val userType by authViewModel.userType.collectAsState()
    // 1) Aplica el filtro
    val filteredEvents by remember(eventLocations, currentCategory) {
        derivedStateOf {
            if (currentCategory != TipoCategoria.NONE) {
                eventLocations.filter { event ->
                    event.categoria == currentCategory
                }
            } else {
                eventLocations
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column {
            SearchTopbar(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(horizontal = 16.dp),
                selectedCategory = currentCategory,
                onCategorySelected = { newCategory -> currentCategory = newCategory }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { clickedLocation ->
                        selectedLocation = clickedLocation
                        selectedEvent = null
                    }
                ) {
                    filteredEvents.forEach { event ->
                        val position = LatLng(event.latitud, event.longitud)
                        val markerState = rememberMarkerState(position = position)

                        Marker(
                            state = markerState,
                            title = event.nombre,
                            snippet = event.descripcion,
                            onClick = {
                                // Alterna la selección del evento.
                                selectedEvent = if (selectedEvent == event) null else event
                                true
                            }
                        )

                    }
                }

                if (userType == "Organizador" || userType == "Asociacion") {
                    FloatingActionButton(
                        onClick = {
                            if (selectedEvent == null) {
                                val location =
                                    selectedLocation ?: cameraPositionState.position.target
                                onLocationSelected(location)
                                navController.navigate("create_event")
                            } else {
                                onMarkerClicked(selectedEvent!!)
                                navController.navigate("create_event")

                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp, bottom = 80.dp),
                        containerColor = if (selectedEvent == null) Color.LightGray else Color.Blue
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Crear Evento")
                    }
                }

                // --- Tarjeta con la información del evento seleccionado ---
                selectedEvent?.let { event ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .align(Alignment.BottomCenter)
                            .background(Color.White.copy(alpha = 0.9f))
                            .padding(8.dp)
                    ) {
                        // Aquí reutilizas tu EventOrganizerMapCard
                        EventOrganizerMapCard(
                            nombre = event.nombre,
                            categoria = event.categoria.name, // Convertimos el enum a String
                            subcategoria = event.subcategoria,
                            lugar = event.lugar,
                            fechaInicio = event.fechaInicio,
                            fechaFin = event.fechaFin,
                            precio = event.precio,
                            aforo = event.aforo,
                            eventId = event.id,
                            navController = navController,
                            images = event.galleryPictureUrls ?: emptyList() // 🔹 Ahora pasamos las imágenes correctas
                        )
                    }
                }
            }
        }
    }
}

