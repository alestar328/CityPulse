package com.app.citypulse.data.dataUsers

import com.app.citypulse.data.enums.AccountType


// Data class para almacenar usuarios con los campos necesarios
data class UserItem(
    val name: String = "",
    val surname: String = "",
    val age: Int = 0,
    val email: String = "",
    val documentId: String? = null,
    val userType: AccountType = AccountType.Persona,
    val valoracion: Int? = null,
    val password: String = "",
    val gender: String? = null  // Género del usuario, puede ser null
)
