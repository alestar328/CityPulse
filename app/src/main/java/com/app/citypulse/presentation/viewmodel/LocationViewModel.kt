package com.app.citypulse.presentation.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LocationViewModel(
    application: Application
) :  AndroidViewModel(application) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(
            getApplication<Application>()
    )
    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation

    fun fetchLocation() {
        // idéntico código, pero usando getApplication() para el permiso
        val ctx = getApplication<Application>()
        if (ContextCompat.checkSelfPermission(
                ctx, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        /* … or COARSE… */
        ) {
            viewModelScope.launch {
                val loc = fusedLocationClient.lastLocation.await()
                loc?.let {
                    _userLocation.value = LatLng(it.latitude, it.longitude)
                }
            }
        }
    }

}