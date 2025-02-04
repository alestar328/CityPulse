package com.app.citypulse.data.dataUsers
import java.util.regex.Pattern




// Data class Usuario
data class UserItem(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val edad: Int,
    val email: String,
    val dni: String,
    val amigos: List<Int>,
    val telefono: String,
    val tipoCuenta: AccountType,
    val valoracion: Int,  // Asumimos que el valor está entre 1 y 5
    val contrasena: String
) {
    init {
        // Validaciones

        // Comprobación de email
        require(email.endsWith("@gmail.com")) { "El correo electrónico debe terminar en '@gmail.com'" }

        // Comprobación de DNI Español (formato básico: 8 dígitos + una letra)
        require(Pattern.matches("\\d{8}[A-Z]", dni)) { "El DNI debe tener el formato correcto (8 dígitos y una letra)" }

        // Comprobación de teléfono (9 dígitos)
        require(telefono.length == 9 && telefono.all { it.isDigit() }) { "El teléfono debe tener exactamente 9 dígitos" }

        // Comprobación de la valoración (del 1 al 5)
        require(valoracion in 1..5) { "La valoración debe estar entre 1 y 5" }
    }
}

