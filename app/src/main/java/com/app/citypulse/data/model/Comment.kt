package com.app.citypulse.data.model

import java.util.Date

data class Comment(
    val id: String = "",
    val eventId: String = "",
    val userId: String = "",
    val organizerId : String = "",
    val userName: String = "",
    val userProfileUrl: String? = null,
    val message: String? = null,
    val rating: Int? = null,
    val createdAt: Date = Date()
)
