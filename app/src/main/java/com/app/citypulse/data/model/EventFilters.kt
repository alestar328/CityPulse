package com.app.citypulse.data.model

import com.app.citypulse.data.enums.TipoCategoria
import com.google.android.gms.maps.model.LatLng

data class EventFilters(
    val category: TipoCategoria = TipoCategoria.NONE,
    val subcategories: Set<String> = emptySet(),
    val rating: Float? = null,
    val userLocation: LatLng? = null,    // ubicación del usuario
    val radiusMeters: Double? = null     // radio de búsqueda
)
