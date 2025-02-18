package com.app.citypulse.data.dataUsers

import java.util.regex.Pattern


// Data class para almacenar usuarios con los campos necesarios
data class UserItem(
    val name: String,                // Nombre del usuario
    val surname: String,             // Apellido del usuario
    val age: Int,                    // Edad del usuario
    val email: String,               // Correo electrónico del usuario
    val documentId: String?,         // DNI o NIF (puede ser null)
    val userType: AccountType = AccountType.Persona, // Tipo de usuario, con valor predeterminado "persona"
    val valoracion: Int? = null,     // Valoración, puede ser null si es un nuevo usuario
    val password: String,            // Contraseña del usuario
    val gender: String? = null       // Género del usuario, puede ser null
)
