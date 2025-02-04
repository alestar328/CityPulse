package com.app.citypulse.data.model

data class EventEntity (

    val id: Int = 0,
    val nombre: String = "",
    val categoria: String = "",
    val subcategoria: String = "",
    val descripcion: String = "",
    val lugar: String = "",
    val fecha: String = "",
    val aforo: Int = 0,
    val precio: Int = 0,
    val valoracion: Int = 0,
    val idRealizador: Int = 0

)