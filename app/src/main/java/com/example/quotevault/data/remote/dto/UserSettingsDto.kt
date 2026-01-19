package com.example.quotevault.data.remote.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSettingsDto(
    @SerialName("id")
    val id: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("theme_mode")
    val themeMode: String = "system",

    @SerialName("accent_color")
    val accentColor: String = "default",

    @SerialName("font_size")
    val fontSize: String = "medium",

    @SerialName("notification_enabled")
    val notificationEnabled: Boolean = true,

    @SerialName("notification_time")
    val notificationTime: String = "09:00:00",

    @SerialName("updated_at")
    val updatedAt: Instant? = null
)

@Serializable
data class UserSettingsInsertDto(
    @SerialName("user_id")
    val userId: String,

    @SerialName("theme_mode")
    val themeMode: String = "system",

    @SerialName("accent_color")
    val accentColor: String = "default",

    @SerialName("font_size")
    val fontSize: String = "medium",

    @SerialName("notification_enabled")
    val notificationEnabled: Boolean = true,

    @SerialName("notification_time")
    val notificationTime: String = "09:00:00"
)

@Serializable
data class UserSettingsUpdateDto(
    @SerialName("theme_mode")
    val themeMode: String? = null,

    @SerialName("accent_color")
    val accentColor: String? = null,

    @SerialName("font_size")
    val fontSize: String? = null,

    @SerialName("notification_enabled")
    val notificationEnabled: Boolean? = null,

    @SerialName("notification_time")
    val notificationTime: String? = null
)
