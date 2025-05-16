package com.app.citypulse.presentation.viewmodel

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LocationViewModel(
    private val fusedLocationClient: FusedLocationProviderClient
) : ViewModel() {
    private val _hasPermission = MutableStateFlow(false)
    val hasPermission: StateFlow<Boolean> = _hasPermission

    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation

    fun setPermission(granted: Boolean) {
        _hasPermission.value = granted
        if (granted) fetchLocation()
    }
    private fun fetchLocation() {
        viewModelScope.launch {
            try {
                val loc = fusedLocationClient.lastLocation.await()
                loc?.let {
                    _userLocation.value = LatLng(it.latitude, it.longitude)
                }
            } catch (e: Exception) {
                // log error
            }
        }
    }
}