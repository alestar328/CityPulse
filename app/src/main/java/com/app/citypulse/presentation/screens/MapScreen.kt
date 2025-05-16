package com.app.citypulse.presentation.screens

import android.util.Log
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
import androidx.navigation.NavController
import com.app.citypulse.data.model.EventUiModel
import com.app.citypulse.presentation.components.EventOrganizerMapCard
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.app.citypulse.data.enums.TipoCategoria
import com.app.citypulse.presentation.ui.theme.TurkBlue
import com.app.citypulse.presentation.viewmodel.UserViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory

@Composable
fun MapScreen(
    userViewModel: UserViewModel,
    viewModel: EventViewModel,
    onLocationSelected: (LatLng) -> Unit,
    onMarkerClicked: (EventUiModel) -> Unit,
    navController: NavController,
    authViewModel: AuthViewModel,
    innerPadding: PaddingValues,
    selectedCategory: TipoCategoria,
    eventLocations: List<EventUiModel>,
    searchEventId: String?
) {
    val cameraPositionState = rememberCameraPositionState()


    var selectedEvent by remember { mutableStateOf<EventUiModel?>(null) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) } //coge ubicacion al presionar dedo

    val userType by authViewModel.userType.collectAsState()


    val filteredEvents = remember(selectedCategory, eventLocations) {
        if (selectedCategory == TipoCategoria.NONE) eventLocations
        else eventLocations.filter { it.categoria == selectedCategory }
    }

    val categoryColors = mapOf(
        TipoCategoria.GASTRONOMICO to 30f,  // 🍊 Naranja
        TipoCategoria.FIESTA to 0f,        // ⚫ Negro
        TipoCategoria.CULTURAL to 120f     // 🟢 Verde
    )
    LaunchedEffect(searchEventId) {
        val evt = searchEventId
            ?.let { id -> eventLocations.find { it.id == id } }

        if (evt != null) {
            Log.d("MapScreen", "Buscando evento desde búsqueda: ${evt.nombre}")
            selectedEvent = evt

            // 3) Y luego animamos la cámara a su posición
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(evt.latitud, evt.longitud),
                    15f
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column {
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
                        key(event.id) {
                            val position = LatLng(event.latitud, event.longitud)
                            val markerColor =
                                categoryColors[event.categoria] ?: BitmapDescriptorFactory.HUE_RED
                            Marker(
                                state = remember { MarkerState(position = position) },
                                title = event.nombre,
                                snippet = event.descripcion,
                                icon = BitmapDescriptorFactory.defaultMarker(markerColor),
                                onClick = {
                                    // Alterna la selección del evento.
                                    selectedEvent = if (selectedEvent == event) null else event
                                    true
                                }
                            )
                        }

                    }
                }

                if (userType == "COMPANY" || userType == "ONG") {
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
                        containerColor = TurkBlue
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Crear Evento",
                            tint = Color.White,
                        )
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
                            nomOrg = event.nomOrg,
                            categoria = event.categoria.name, // Convertimos el enum a String
                            subcategoria = event.subcategoria,
                            lugar = event.lugar,
                            fechaInicio = event.fechaInicio,
                            fechaFin = event.fechaFin,
                            precio = event.precio,
                            aforo = event.aforo,
                            eventId = event.id,
                            navController = navController,
                            onSubscribe = {
                                userViewModel.saveEventForUser(event.id)
                                navController.navigate("saved_events")
                            },
                            images = event.galleryPictureUrls
                                ?: emptyList() // 🔹 Ahora pasamos las imágenes correctas
                        )
                    }
                }
            }
        }
    }
}

