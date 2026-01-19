package com.example.quotevault.data.mapper

import com.example.quotevault.data.remote.dto.UserProfileDto
import com.example.quotevault.domain.model.User
import io.github.jan.supabase.auth.user.UserInfo

fun UserProfileDto.toDomain(email: String): User {
    return User(
        id = id,
        email = email,
        displayName = displayName,
        avatarUrl = avatarUrl,
        createdAt = createdAt
    )
}

fun UserInfo.toDomain(profile: UserProfileDto? = null): User {
    return User(
        id = id,
        email = email ?: "",
        displayName = profile?.displayName,
        avatarUrl = profile?.avatarUrl,
        createdAt = profile?.createdAt
    )
}
