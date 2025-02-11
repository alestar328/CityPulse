package com.app.citypulse.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.citypulse.presentation.EventViewModel
import com.app.citypulse.presentation.components.SearchTopbar
import com.app.citypulse.presentation.components.SearcherBar
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

@Composable
fun MapScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // Simula el mapa en el preview
    ) {
        SearchTopbar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()

                .padding(16.dp)
                .align(Alignment.TopCenter) // Simula la barra sobre el mapa

        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMapScreen() {
    MapScreenPreview()
}