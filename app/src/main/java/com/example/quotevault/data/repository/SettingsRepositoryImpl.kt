package com.example.quotevault.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.quotevault.core.common.Constants
import com.example.quotevault.data.remote.dto.UserSettingsDto
import com.example.quotevault.data.remote.dto.UserSettingsInsertDto
import com.example.quotevault.data.remote.dto.UserSettingsUpdateDto
import com.example.quotevault.domain.model.UserSettings
import com.example.quotevault.domain.model.toAccentColor
import com.example.quotevault.domain.model.toFontSizePreference
import com.example.quotevault.domain.model.toStorageString
import com.example.quotevault.domain.model.toThemeMode
import com.example.quotevault.domain.repository.SettingsRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val supabaseClient: SupabaseClient
) : SettingsRepository {

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey(Constants.Prefs.THEME_MODE)
        val ACCENT_COLOR = stringPreferencesKey(Constants.Prefs.ACCENT_COLOR)
        val FONT_SIZE = stringPreferencesKey(Constants.Prefs.FONT_SIZE)
        val NOTIFICATION_ENABLED = booleanPreferencesKey(Constants.Prefs.NOTIFICATION_ENABLED)
        val NOTIFICATION_TIME = stringPreferencesKey(Constants.Prefs.NOTIFICATION_TIME)
    }

    private val userId: String?
        get() = supabaseClient.auth.currentUserOrNull()?.id

    override val settings: Flow<UserSettings> = dataStore.data.map { preferences ->
        UserSettings(
            themeMode = (preferences[PreferencesKeys.THEME_MODE] ?: "system").toThemeMode(),
            accentColor = (preferences[PreferencesKeys.ACCENT_COLOR] ?: "default").toAccentColor(),
            fontSize = (preferences[PreferencesKeys.FONT_SIZE] ?: "medium").toFontSizePreference(),
            notificationEnabled = preferences[PreferencesKeys.NOTIFICATION_ENABLED] ?: true,
            notificationTime = parseTime(preferences[PreferencesKeys.NOTIFICATION_TIME] ?: "09:00")
        )
    }

    override suspend fun getSettings(): UserSettings {
        return settings.first()
    }

    override suspend fun updateSettings(settings: UserSettings): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.THEME_MODE] = settings.themeMode.toStorageString()
                preferences[PreferencesKeys.ACCENT_COLOR] = settings.accentColor.toStorageString()
                preferences[PreferencesKeys.FONT_SIZE] = settings.fontSize.toStorageString()
                preferences[PreferencesKeys.NOTIFICATION_ENABLED] = settings.notificationEnabled
                preferences[PreferencesKeys.NOTIFICATION_TIME] = formatTime(settings.notificationTime)
            }

            // Sync to remote if logged in
            val uid = userId
            if (uid != null) {
                try {
                    val existingSettings = supabaseClient.postgrest[Constants.Tables.USER_SETTINGS]
                        .select { filter { eq("user_id", uid) } }
                        .decodeSingleOrNull<UserSettingsDto>()

                    if (existingSettings != null) {
                        supabaseClient.postgrest[Constants.Tables.USER_SETTINGS]
                            .update(
                                UserSettingsUpdateDto(
                                    themeMode = settings.themeMode.toStorageString(),
                                    accentColor = settings.accentColor.toStorageString(),
                                    fontSize = settings.fontSize.toStorageString(),
                                    notificationEnabled = settings.notificationEnabled,
                                    notificationTime = formatTime(settings.notificationTime) + ":00"
                                )
                            ) {
                                filter { eq("user_id", uid) }
                            }
                    } else {
                        supabaseClient.postgrest[Constants.Tables.USER_SETTINGS]
                            .insert(
                                UserSettingsInsertDto(
                                    userId = uid,
                                    themeMode = settings.themeMode.toStorageString(),
                                    accentColor = settings.accentColor.toStorageString(),
                                    fontSize = settings.fontSize.toStorageString(),
                                    notificationEnabled = settings.notificationEnabled,
                                    notificationTime = formatTime(settings.notificationTime) + ":00"
                                )
                            )
                    }
                } catch (e: Exception) {
                    // Ignore remote errors, local is saved
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncSettings(): Result<Unit> {
        val uid = userId ?: return Result.failure(Exception("Not logged in"))

        return try {
            val remoteSettings = supabaseClient.postgrest[Constants.Tables.USER_SETTINGS]
                .select { filter { eq("user_id", uid) } }
                .decodeSingleOrNull<UserSettingsDto>()

            if (remoteSettings != null) {
                dataStore.edit { preferences ->
                    preferences[PreferencesKeys.THEME_MODE] = remoteSettings.themeMode
                    preferences[PreferencesKeys.ACCENT_COLOR] = remoteSettings.accentColor
                    preferences[PreferencesKeys.FONT_SIZE] = remoteSettings.fontSize
                    preferences[PreferencesKeys.NOTIFICATION_ENABLED] = remoteSettings.notificationEnabled
                    preferences[PreferencesKeys.NOTIFICATION_TIME] = remoteSettings.notificationTime.take(5)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseTime(timeString: String): LocalTime {
        return try {
            val parts = timeString.split(":")
            LocalTime(parts[0].toInt(), parts.getOrNull(1)?.toInt() ?: 0)
        } catch (e: Exception) {
            LocalTime(9, 0)
        }
    }

    private fun formatTime(time: LocalTime): String {
        return "${time.hour.toString().padStart(2, '0')}:${time.minute.toString().padStart(2, '0')}"
    }
}
