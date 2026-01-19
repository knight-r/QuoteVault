package com.example.quotevault.domain.repository

import com.example.quotevault.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    val isLoggedIn: Flow<Boolean>

    suspend fun signUp(email: String, password: String): Result<User>
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signOut(): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun getCurrentUser(): User?
    suspend fun updateProfile(displayName: String?, avatarUrl: String?): Result<User>
    suspend fun uploadAvatar(bytes: ByteArray): Result<String>
}
