package com.app.citypulse.data.model

import com.app.citypulse.data.enums.TipoCategoria

data class EventUiModel(
    val id: String,
    val nomOrg : String,
    val nombre: String,
    val categoria: TipoCategoria, // Usamos el tipo del enum
    val subcategoria: String,
    val descripcion: String,
    val lugar: String,
    val latitud: Double,
    val longitud: Double,
    val fechaInicio: String,
    val fechaFin: String,
    val aforo: Int,
    val precio: Double,
    val valoracion: Int,
    val idRealizador: String = "",
    val galleryPictureUrls: List<String> = emptyList() // 🔹 Nueva propiedad para imágenes

)
