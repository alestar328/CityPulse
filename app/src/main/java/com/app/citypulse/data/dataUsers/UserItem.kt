package com.app.citypulse.data.dataUsers

import java.util.regex.Pattern


// Data class para almacenar usuarios
data class UserItem(
    val id: Int? = null,
    val name: String,
    val surname: String,
    val age: Int,
    val email: String,
    val documentId: String,  // Puede ser DNI o NIF
    val amigos: List<Int> = emptyList(),
    val telefono: String? = null,
    val userType: String = AccountType.Persona.name,  // Guardar enum como String
    val valoracion: Int? = null,  // Puede ser null si es un nuevo usuario
    val password: String,
    val gender: String,
    val fiscalAddress: String? = null  // Solo se usa en cuentas de empresa
) {
    constructor() : this(null, "", "", 0, "", "", emptyList(), null, AccountType.Persona.name, null, "", "", null)

    init {
        if (email.isNotEmpty()) require(email.endsWith("@gmail.com")) { "El correo electrónico debe terminar en '@gmail.com'" }

        if (documentId.isNotEmpty()) {
            if (userType == AccountType.Persona.name) {
                require(Pattern.matches("\\d{8}[A-Z]", documentId)) { "El DNI debe tener el formato correcto (8 dígitos y una letra)" }
            } else {
                require(Pattern.matches("[A-Z]\\d{8}", documentId)) { "El NIF debe tener el formato correcto (1 letra seguida de 8 números)" }
                require(fiscalAddress != null) { "Las cuentas de empresa deben incluir una dirección fiscal" }
            }
        }

        telefono?.let {
            require(it.length == 9 && it.all { char -> char.isDigit() }) { "El teléfono debe tener exactamente 9 dígitos" }
        }

        valoracion?.let {
            require(it in 1..5) { "La valoración debe estar entre 1 y 5" }
        }
    }
}
