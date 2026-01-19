package com.example.quotevault.domain.model

import com.example.quotevault.core.ui.theme.AccentColor
import com.example.quotevault.core.ui.theme.FontSizePreference
import com.example.quotevault.core.ui.theme.ThemeMode
import kotlinx.datetime.LocalTime

data class UserSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val accentColor: AccentColor = AccentColor.DEFAULT,
    val fontSize: FontSizePreference = FontSizePreference.MEDIUM,
    val notificationEnabled: Boolean = true,
    val notificationTime: LocalTime = LocalTime(9, 0)
) {
    companion object {
        val DEFAULT = UserSettings()
    }
}

fun String.toThemeMode(): ThemeMode = when (this.lowercase()) {
    "light" -> ThemeMode.LIGHT
    "dark" -> ThemeMode.DARK
    else -> ThemeMode.SYSTEM
}

fun ThemeMode.toStorageString(): String = when (this) {
    ThemeMode.LIGHT -> "light"
    ThemeMode.DARK -> "dark"
    ThemeMode.SYSTEM -> "system"
}

fun String.toAccentColor(): AccentColor = when (this.lowercase()) {
    "blue" -> AccentColor.BLUE
    "green" -> AccentColor.GREEN
    "purple" -> AccentColor.PURPLE
    "orange" -> AccentColor.ORANGE
    else -> AccentColor.DEFAULT
}

fun AccentColor.toStorageString(): String = when (this) {
    AccentColor.DEFAULT -> "default"
    AccentColor.BLUE -> "blue"
    AccentColor.GREEN -> "green"
    AccentColor.PURPLE -> "purple"
    AccentColor.ORANGE -> "orange"
}

fun String.toFontSizePreference(): FontSizePreference = when (this.lowercase()) {
    "small" -> FontSizePreference.SMALL
    "large" -> FontSizePreference.LARGE
    "extra_large" -> FontSizePreference.EXTRA_LARGE
    else -> FontSizePreference.MEDIUM
}

fun FontSizePreference.toStorageString(): String = when (this) {
    FontSizePreference.SMALL -> "small"
    FontSizePreference.MEDIUM -> "medium"
    FontSizePreference.LARGE -> "large"
    FontSizePreference.EXTRA_LARGE -> "extra_large"
}
