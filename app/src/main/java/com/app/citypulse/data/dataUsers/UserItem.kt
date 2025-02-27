package com.app.citypulse.data.dataUsers

data class UserItem @JvmOverloads constructor(
    val name: String = "",                // Nombre del usuario
    val surname: String = "",             // Apellido del usuario
    val age: Int = 0,                     // Edad del usuario
    val email: String = "",               // Correo electrónico del usuario
    val documentId: String? = null,       // DNI o NIF (puede ser null)
    val userType: AccountType = AccountType.Persona, // Tipo de usuario, con valor predeterminado "persona"
    val valoracion: Int? = null,          // Valoración, puede ser null si es un nuevo usuario
    val password: String = "",            // Contraseña del usuario
    val gender: String? = null,           // Género del usuario, puede ser null
    val uid: String? = null,
    val google: String? = null,
    val friends: MutableList<String> = mutableListOf() // Lista de amigos por UID
)