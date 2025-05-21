package com.app.citypulse.data.model

import com.app.citypulse.data.enums.TipoCategoria
import com.google.android.gms.maps.model.LatLng

data class EventFilters(
    val categories: Set<TipoCategoria> = emptySet(),
    val subcategories: Set<String>      = emptySet(),
    val rating: Float?                  = null,
    val userLocation: LatLng?           = null,
    val radiusMeters: Double?           = null
)
