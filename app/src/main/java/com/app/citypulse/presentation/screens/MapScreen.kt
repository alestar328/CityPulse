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
import com.app.citypulse.presentation.components.SearchTopbar
import com.app.citypulse.presentation.components.SearcherBar
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen() {
    val barna = LatLng(41.3870540940097, 2.1702107801979693)
    val barnaState = MarkerState(position = barna)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(barna, 15f)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = barnaState,
                title = "Marcador en Barcelona"
            )
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