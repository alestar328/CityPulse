package com.app.citypulse.data.model

enum class TipoCategoria(val displayName: String) {
    GASTRONOMICO("Gastronómico"),
    CULTURAL("Cultural"),
    FIESTA("Fiesta");

    companion object {
        fun fromDisplayName(name: String): TipoCategoria? {
            return values().find { it.displayName == name }
        }
    }
}
