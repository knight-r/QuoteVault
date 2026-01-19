package com.example.quotevault.data.repository

import com.example.quotevault.core.common.Constants
import com.example.quotevault.data.mapper.toDomain
import com.example.quotevault.data.remote.dto.UserProfileDto
import com.example.quotevault.data.remote.dto.UserProfileInsertDto
import com.example.quotevault.data.remote.dto.UserProfileUpdateDto
import com.example.quotevault.domain.model.User
import com.example.quotevault.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : AuthRepository {

    override val currentUser: Flow<User?> = supabaseClient.auth.sessionStatus.map { status ->
        when (status) {
            is SessionStatus.Authenticated -> {
                val userInfo = status.session.user
                if (userInfo != null) {
                    val profile = getProfile(userInfo.id)
                    userInfo.toDomain(profile)
                } else null
            }
            else -> null
        }
    }

    override val isLoggedIn: Flow<Boolean> = supabaseClient.auth.sessionStatus.map { status ->
        status is SessionStatus.Authenticated
    }

    override suspend fun signUp(email: String, password: String): Result<User> {
        return try {
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            val user = supabaseClient.auth.currentUserOrNull()
            if (user != null) {
                // Create user profile
                createProfile(user.id)
                val profile = getProfile(user.id)
                Result.success(user.toDomain(profile))
            } else {
                Result.failure(Exception("Sign up failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val user = supabaseClient.auth.currentUserOrNull()
            if (user != null) {
                val profile = getProfile(user.id)
                Result.success(user.toDomain(profile))
            } else {
                Result.failure(Exception("Sign in failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            supabaseClient.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            supabaseClient.auth.resetPasswordForEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        val userInfo = supabaseClient.auth.currentUserOrNull() ?: return null
        val profile = getProfile(userInfo.id)
        return userInfo.toDomain(profile)
    }

    override suspend fun updateProfile(displayName: String?, avatarUrl: String?): Result<User> {
        return try {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
                ?: return Result.failure(Exception("Not logged in"))

            supabaseClient.postgrest[Constants.Tables.USER_PROFILES]
                .update(UserProfileUpdateDto(displayName = displayName, avatarUrl = avatarUrl)) {
                    filter { eq("id", userId) }
                }

            val user = getCurrentUser() ?: return Result.failure(Exception("Failed to get user"))
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadAvatar(bytes: ByteArray): Result<String> {
        return try {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
                ?: return Result.failure(Exception("Not logged in"))

            val fileName = "avatars/$userId/${UUID.randomUUID()}.jpg"
            supabaseClient.storage["avatars"].upload(fileName, bytes) {
                upsert = true
            }

            val publicUrl = supabaseClient.storage["avatars"].publicUrl(fileName)
            Result.success(publicUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getProfile(userId: String): UserProfileDto? {
        return try {
            supabaseClient.postgrest[Constants.Tables.USER_PROFILES]
                .select { filter { eq("id", userId) } }
                .decodeSingleOrNull<UserProfileDto>()
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun createProfile(userId: String) {
        try {
            supabaseClient.postgrest[Constants.Tables.USER_PROFILES]
                .insert(UserProfileInsertDto(id = userId))
        } catch (e: Exception) {
            // Profile might already exist
        }
    }
}
