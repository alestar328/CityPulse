package com.app.citypulse.data.model

import java.util.Date

data class EventEntity (
    val id: Int = 0,
    val nombre: String = "",
    val categoria: String = "",
    val subcategoria: String = "",
    val descripcion: String = "",
    val lugar: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val fechaInicio: Date? = null,
    val fechaFin: Date? = null,
    val aforo: Int = 0,
    val precio: Double = 0.0,
    val valoracion: Int = 0,
    val idRealizador: Int = 0
) {
    fun obtenerUbicacion(): String {
        return if (latitud != 0.0 && longitud != 0.0) {
            "Lat: $latitud, Lng: $longitud"
        } else {
            "Ubicación no seleccionada"
        }
    }
}