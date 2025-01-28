package com.app.citypulse.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(){
    val barna = LatLng(41.3870540940097, 2.1702107801979693)
    val barnaState = MarkerState(position = barna)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(barna, 10f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ){
        Marker(
            state = barnaState,
            title = "Marcador en Barcelona"
        )

    }

}