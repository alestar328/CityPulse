package com.app.citypulse.data.enums

enum class TipoCategoria(val displayName: String? = null) {
    GASTRONOMICO("Gastronómico"),
    CULTURAL("Cultural"),
    FIESTA("Fiesta"),
    NONE;

    companion object {
        fun fromDisplayName(name: String): TipoCategoria? {
            return values().find { it.displayName == name }
        }
    }
}
