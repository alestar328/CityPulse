package com.app.citypulse.presentation.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.citypulse.data.model.EventUiModel
import com.app.citypulse.presentation.components.EventOrganizerMapCard
import com.app.citypulse.presentation.viewmodel.AuthViewModel
import com.app.citypulse.presentation.viewmodel.EventViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.app.citypulse.data.enums.TipoCategoria
import com.app.citypulse.presentation.ui.theme.TurkBlue
import com.app.citypulse.presentation.viewmodel.LocationViewModel
import com.app.citypulse.presentation.viewmodel.UserViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    userViewModel: UserViewModel,
    viewModel: EventViewModel,
    locationViewModel: LocationViewModel,
    onLocationSelected: (LatLng) -> Unit,
    onMarkerClicked: (EventUiModel) -> Unit,
    navController: NavController,
    authViewModel: AuthViewModel,
    innerPadding: PaddingValues,
    selectedCategory: TipoCategoria,
    eventLocations: List<EventUiModel>,
    searchEventId: String?
) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    val userLocation by locationViewModel.userLocation.collectAsState()
    val mapUiScope = rememberCoroutineScope()

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
    // 1. Create a permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseLocationGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (fineLocationGranted || coarseLocationGranted) {
                // Permission granted, fetch location
                locationViewModel.fetchLocation()
            } else {
                // Permission denied, handle this case (e.g., show a message to the user)
                Log.d("MapScreen", "Location permission denied.")
            }
        }
    )

    // 2. Request permission when the screen starts or when needed
    LaunchedEffect(Unit) {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        when {
            hasFineLocationPermission || hasCoarseLocationPermission -> {
                // Permission already granted, fetch location
                locationViewModel.fetchLocation()
            }

            else -> {
                // Request permission
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }
    LaunchedEffect(searchEventId) {
        searchEventId
            ?.let { id -> eventLocations.find { it.id == id } }
            ?.let { evt ->
                mapUiScope.launch {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(evt.latitud, evt.longitud),
                            15f
                        )
                    )
                }
                selectedEvent = evt

            }
    }
    // 3. Move the camera to the user's location once it's available
    LaunchedEffect(userLocation, searchEventId) {
        // solo si no había búsqueda activa
        if (searchEventId.isNullOrEmpty()) {
            userLocation?.let { latLng ->
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                )
            }
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
                    properties = MapProperties(
                        isMyLocationEnabled = true        // activa el punto azul
                    ),
                    uiSettings = MapUiSettings(
                        myLocationButtonEnabled = true, //Ubi propia
                        zoomControlsEnabled = false
                    ),
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
                    userLocation?.let { myLoc ->
                        // círculo de 50m de radio
                        Circle(
                            center = myLoc,
                            radius = 50.0,                   // en metros
                            strokeColor = Color.Blue.copy(alpha = 0.5f),
                            fillColor = Color.Blue.copy(alpha = 0.1f),
                            strokeWidth = 2f
                        )
                    }
                }
                FloatingActionButton(
                    onClick = {
                        userLocation?.let { loc ->
                            // no llamar a animate() directamente: envuélvelo en un launch
                            mapUiScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(loc, 15f)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 80.dp),
                    containerColor = Color.White,            // o el color que prefieras
                    elevation = FloatingActionButtonDefaults.elevation(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Mi ubicación",
                        tint = TurkBlue
                    )
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

                //Boton crear evento
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
            }
        }
    }
}

