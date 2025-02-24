package com.app.citypulse.data.model

import java.util.Date

data class EventEntity(
    val id: String = "",
    val nombre: String = "",
    val categoria: TipoCategoria = TipoCategoria.CULTURAL,
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
    val idRealizador: String = ""
)
