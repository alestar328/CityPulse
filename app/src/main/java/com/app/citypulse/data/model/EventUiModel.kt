package com.app.citypulse.data.model

data class EventUiModel(
    val id: String,
    val nombre: String,
    val categoria: String,
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
    val idRealizador: String = ""
)
