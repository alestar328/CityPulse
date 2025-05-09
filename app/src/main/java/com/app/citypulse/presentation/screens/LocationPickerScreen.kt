package com.app.citypulse.presentation.screens

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.io.IOException
import java.util.*

@Composable
fun LocationPickerScreen(
    navController: NavController,
    innerPadding: PaddingValues
) {
    val context = LocalContext.current
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var selectedAddress by remember { mutableStateOf("Selecciona una ubicación") }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(41.57008436408339, 1.9954403499999671), 12f)
    }
    val markerState = rememberMarkerState(position = LatLng(41.387054, 2.170210))

    fun getAddressFromLatLng(context: Context, latLng: LatLng): String {
        // Geocoder para obtener la dirección en texto a partir de las coordenadas.
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                addresses[0].getAddressLine(0) ?: "Dirección no encontrada"
            } else {
                "Dirección no encontrada"
            }
        } catch (e: IOException) {
            Log.e("LocationPickerScreen", "Error obteniendo dirección", e)
            "Dirección no encontrada"
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                selectedLocation = latLng
                selectedAddress = getAddressFromLatLng(context, latLng)
                markerState.position = latLng

                Log.d("LocationPickerScreen", "Ubicación seleccionada: Lat: ${latLng.latitude}, Lng: ${latLng.longitude}")
            }
        ) {
            selectedLocation?.let {
                Marker(state = markerState, title = "Ubicación seleccionada")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(text = "Ubicación: $selectedAddress", style = MaterialTheme.typography.bodyLarge)

            selectedLocation?.let { location ->
                Button(
                    onClick = {
                        selectedLocation?.let { location ->
                            navController.previousBackStackEntry?.savedStateHandle?.apply {
                                set("latitud", location.latitude)
                                set("longitud", location.longitude)
                                set("direccion", selectedAddress)
                            }
                            navController.popBackStack()
                        }
                    }
                ) {
                    Text("Confirmar Ubicación")
                }
            }
        }
    }
}

