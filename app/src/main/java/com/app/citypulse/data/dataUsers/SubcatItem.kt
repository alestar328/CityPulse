package com.app.citypulse.data.dataUsers

import com.app.citypulse.data.enums.TipoCategoria
import com.google.firebase.Timestamp

data class SubcatItem(
    val id: String = "",
    val name: String = "",
    val category: TipoCategoria = TipoCategoria.NONE,
    val createdAt: Timestamp? = null,
    val image: String? = null,
    val description: String? = null
)
