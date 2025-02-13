package com.app.citypulse.data.model

import com.app.citypulse.data.dataUsers.AccountType

data class tempUserData(
    val email: String,
    val password: String,
    val name: String? = null,
    val surname: String? = null,
    val age: Int? = null,
    val documentId: String? = null,
    val gender: String? = null,
    val fiscalAddress: String? = null,
    val userType: AccountType
)
