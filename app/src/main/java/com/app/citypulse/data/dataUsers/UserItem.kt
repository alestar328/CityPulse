package com.app.citypulse.data.dataUsers

import com.app.citypulse.data.enums.AccountType


// Data class para almacenar usuarios con los campos necesarios
data class UserItem @JvmOverloads constructor(
    val name: String = "",                // Nombre del usuario
    val surname: String = "",             // Apellido del usuario
    val age: Int = 0,                     // Edad del usuario
    val email: String = "",               // Correo electrónico del usuario
    val documentId: String? = null,       // DNI o NIF (puede ser null)
    val userType: AccountType = AccountType.Persona, // Tipo de usuario, con valor predeterminado "persona"
    val valoracion: Int? = null,          // Valoración, puede ser null si es un nuevo usuario
    val password: String = "",            // Contraseña del usuario
    val gender: String? = null,           // Género del usuario, puede ser null
    val language: String = "es",  // Nuevo campo para el idioma con valor por defecto "es"
    val uid: String? = null,
    val google: String? = null,
    val friends: MutableList<String> = mutableListOf(),
    val photoUrls: MutableList<String> = mutableListOf(),
    val eventAssit : MutableList<String> = mutableListOf()
)