package com.example.quotevault.domain.repository

import com.example.quotevault.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<UserSettings>

    suspend fun getSettings(): UserSettings
    suspend fun updateSettings(settings: UserSettings): Result<Unit>
    suspend fun syncSettings(): Result<Unit>
}
