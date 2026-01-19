package com.example.quotevault.domain.model

import kotlinx.datetime.Instant

data class User(
    val id: String,
    val email: String,
    val displayName: String? = null,
    val avatarUrl: String? = null,
    val createdAt: Instant? = null
) {
    val initials: String
        get() = displayName?.split(" ")
            ?.take(2)
            ?.mapNotNull { it.firstOrNull()?.uppercaseChar() }
            ?.joinToString("")
            ?: email.firstOrNull()?.uppercaseChar()?.toString()
            ?: "U"

    val displayNameOrEmail: String
        get() = displayName ?: email.substringBefore("@")
}
