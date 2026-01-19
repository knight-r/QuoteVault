package com.example.quotevault.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Theme Mode
enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

// Accent Color Options
enum class AccentColor {
    DEFAULT, BLUE, GREEN, PURPLE, ORANGE
}

// Light Color Schemes for each accent
private fun lightColorScheme(accentColor: AccentColor) = when (accentColor) {
    AccentColor.DEFAULT -> lightColorScheme(
        primary = PrimaryDefault,
        onPrimary = OnPrimaryDefault,
        primaryContainer = PrimaryContainerDefault,
        onPrimaryContainer = OnPrimaryContainerDefault,
        secondary = SecondaryLight,
        onSecondary = OnSecondaryLight,
        secondaryContainer = SecondaryContainerLight,
        onSecondaryContainer = SecondaryLight,
        tertiary = TertiaryLight,
        onTertiary = OnTertiaryLight,
        tertiaryContainer = TertiaryContainerLight,
        onTertiaryContainer = TertiaryLight,
        background = BackgroundLight,
        onBackground = OnBackgroundLight,
        surface = SurfaceLight,
        onSurface = OnSurfaceLight,
        surfaceVariant = SurfaceVariantLight,
        onSurfaceVariant = OnSurfaceVariantLight,
        error = ErrorLight,
        onError = OnErrorLight,
        errorContainer = ErrorContainerLight,
        onErrorContainer = ErrorLight,
        outline = OutlineLight,
        outlineVariant = OutlineVariantLight
    )
    AccentColor.BLUE -> lightColorScheme(
        primary = PrimaryBlue,
        onPrimary = OnPrimaryBlue,
        primaryContainer = PrimaryContainerBlue,
        onPrimaryContainer = OnPrimaryContainerBlue,
        secondary = SecondaryLight,
        onSecondary = OnSecondaryLight,
        secondaryContainer = SecondaryContainerLight,
        onSecondaryContainer = SecondaryLight,
        tertiary = TertiaryLight,
        onTertiary = OnTertiaryLight,
        tertiaryContainer = TertiaryContainerLight,
        onTertiaryContainer = TertiaryLight,
        background = BackgroundLight,
        onBackground = OnBackgroundLight,
        surface = SurfaceLight,
        onSurface = OnSurfaceLight,
        surfaceVariant = SurfaceVariantLight,
        onSurfaceVariant = OnSurfaceVariantLight,
        error = ErrorLight,
        onError = OnErrorLight,
        errorContainer = ErrorContainerLight,
        onErrorContainer = ErrorLight,
        outline = OutlineLight,
        outlineVariant = OutlineVariantLight
    )
    AccentColor.GREEN -> lightColorScheme(
        primary = PrimaryGreen,
        onPrimary = OnPrimaryGreen,
        primaryContainer = PrimaryContainerGreen,
        onPrimaryContainer = OnPrimaryContainerGreen,
        secondary = SecondaryLight,
        onSecondary = OnSecondaryLight,
        secondaryContainer = SecondaryContainerLight,
        onSecondaryContainer = SecondaryLight,
        tertiary = TertiaryLight,
        onTertiary = OnTertiaryLight,
        tertiaryContainer = TertiaryContainerLight,
        onTertiaryContainer = TertiaryLight,
        background = BackgroundLight,
        onBackground = OnBackgroundLight,
        surface = SurfaceLight,
        onSurface = OnSurfaceLight,
        surfaceVariant = SurfaceVariantLight,
        onSurfaceVariant = OnSurfaceVariantLight,
        error = ErrorLight,
        onError = OnErrorLight,
        errorContainer = ErrorContainerLight,
        onErrorContainer = ErrorLight,
        outline = OutlineLight,
        outlineVariant = OutlineVariantLight
    )
    AccentColor.PURPLE -> lightColorScheme(
        primary = PrimaryPurple,
        onPrimary = OnPrimaryPurple,
        primaryContainer = PrimaryContainerPurple,
        onPrimaryContainer = OnPrimaryContainerPurple,
        secondary = SecondaryLight,
        onSecondary = OnSecondaryLight,
        secondaryContainer = SecondaryContainerLight,
        onSecondaryContainer = SecondaryLight,
        tertiary = TertiaryLight,
        onTertiary = OnTertiaryLight,
        tertiaryContainer = TertiaryContainerLight,
        onTertiaryContainer = TertiaryLight,
        background = BackgroundLight,
        onBackground = OnBackgroundLight,
        surface = SurfaceLight,
        onSurface = OnSurfaceLight,
        surfaceVariant = SurfaceVariantLight,
        onSurfaceVariant = OnSurfaceVariantLight,
        error = ErrorLight,
        onError = OnErrorLight,
        errorContainer = ErrorContainerLight,
        onErrorContainer = ErrorLight,
        outline = OutlineLight,
        outlineVariant = OutlineVariantLight
    )
    AccentColor.ORANGE -> lightColorScheme(
        primary = PrimaryOrange,
        onPrimary = OnPrimaryOrange,
        primaryContainer = PrimaryContainerOrange,
        onPrimaryContainer = OnPrimaryContainerOrange,
        secondary = SecondaryLight,
        onSecondary = OnSecondaryLight,
        secondaryContainer = SecondaryContainerLight,
        onSecondaryContainer = SecondaryLight,
        tertiary = TertiaryLight,
        onTertiary = OnTertiaryLight,
        tertiaryContainer = TertiaryContainerLight,
        onTertiaryContainer = TertiaryLight,
        background = BackgroundLight,
        onBackground = OnBackgroundLight,
        surface = SurfaceLight,
        onSurface = OnSurfaceLight,
        surfaceVariant = SurfaceVariantLight,
        onSurfaceVariant = OnSurfaceVariantLight,
        error = ErrorLight,
        onError = OnErrorLight,
        errorContainer = ErrorContainerLight,
        onErrorContainer = ErrorLight,
        outline = OutlineLight,
        outlineVariant = OutlineVariantLight
    )
}

// Dark Color Schemes for each accent
private fun darkColorScheme(accentColor: AccentColor) = when (accentColor) {
    AccentColor.DEFAULT -> darkColorScheme(
        primary = PrimaryDefaultDark,
        onPrimary = OnPrimaryContainerDefault,
        primaryContainer = OnPrimaryContainerDefault,
        onPrimaryContainer = PrimaryDefaultDark,
        secondary = SecondaryDark,
        onSecondary = OnSecondaryDark,
        secondaryContainer = SecondaryContainerDark,
        onSecondaryContainer = SecondaryDark,
        tertiary = TertiaryDark,
        onTertiary = OnTertiaryDark,
        tertiaryContainer = TertiaryContainerDark,
        onTertiaryContainer = TertiaryDark,
        background = BackgroundDark,
        onBackground = OnBackgroundDark,
        surface = SurfaceDark,
        onSurface = OnSurfaceDark,
        surfaceVariant = SurfaceVariantDark,
        onSurfaceVariant = OnSurfaceVariantDark,
        error = ErrorDark,
        onError = OnErrorDark,
        errorContainer = ErrorContainerDark,
        onErrorContainer = ErrorDark,
        outline = OutlineDark,
        outlineVariant = OutlineVariantDark
    )
    AccentColor.BLUE -> darkColorScheme(
        primary = PrimaryBlueDark,
        onPrimary = OnPrimaryContainerBlue,
        primaryContainer = OnPrimaryContainerBlue,
        onPrimaryContainer = PrimaryBlueDark,
        secondary = SecondaryDark,
        onSecondary = OnSecondaryDark,
        secondaryContainer = SecondaryContainerDark,
        onSecondaryContainer = SecondaryDark,
        tertiary = TertiaryDark,
        onTertiary = OnTertiaryDark,
        tertiaryContainer = TertiaryContainerDark,
        onTertiaryContainer = TertiaryDark,
        background = BackgroundDark,
        onBackground = OnBackgroundDark,
        surface = SurfaceDark,
        onSurface = OnSurfaceDark,
        surfaceVariant = SurfaceVariantDark,
        onSurfaceVariant = OnSurfaceVariantDark,
        error = ErrorDark,
        onError = OnErrorDark,
        errorContainer = ErrorContainerDark,
        onErrorContainer = ErrorDark,
        outline = OutlineDark,
        outlineVariant = OutlineVariantDark
    )
    AccentColor.GREEN -> darkColorScheme(
        primary = PrimaryGreenDark,
        onPrimary = OnPrimaryContainerGreen,
        primaryContainer = OnPrimaryContainerGreen,
        onPrimaryContainer = PrimaryGreenDark,
        secondary = SecondaryDark,
        onSecondary = OnSecondaryDark,
        secondaryContainer = SecondaryContainerDark,
        onSecondaryContainer = SecondaryDark,
        tertiary = TertiaryDark,
        onTertiary = OnTertiaryDark,
        tertiaryContainer = TertiaryContainerDark,
        onTertiaryContainer = TertiaryDark,
        background = BackgroundDark,
        onBackground = OnBackgroundDark,
        surface = SurfaceDark,
        onSurface = OnSurfaceDark,
        surfaceVariant = SurfaceVariantDark,
        onSurfaceVariant = OnSurfaceVariantDark,
        error = ErrorDark,
        onError = OnErrorDark,
        errorContainer = ErrorContainerDark,
        onErrorContainer = ErrorDark,
        outline = OutlineDark,
        outlineVariant = OutlineVariantDark
    )
    AccentColor.PURPLE -> darkColorScheme(
        primary = PrimaryPurpleDark,
        onPrimary = OnPrimaryContainerPurple,
        primaryContainer = OnPrimaryContainerPurple,
        onPrimaryContainer = PrimaryPurpleDark,
        secondary = SecondaryDark,
        onSecondary = OnSecondaryDark,
        secondaryContainer = SecondaryContainerDark,
        onSecondaryContainer = SecondaryDark,
        tertiary = TertiaryDark,
        onTertiary = OnTertiaryDark,
        tertiaryContainer = TertiaryContainerDark,
        onTertiaryContainer = TertiaryDark,
        background = BackgroundDark,
        onBackground = OnBackgroundDark,
        surface = SurfaceDark,
        onSurface = OnSurfaceDark,
        surfaceVariant = SurfaceVariantDark,
        onSurfaceVariant = OnSurfaceVariantDark,
        error = ErrorDark,
        onError = OnErrorDark,
        errorContainer = ErrorContainerDark,
        onErrorContainer = ErrorDark,
        outline = OutlineDark,
        outlineVariant = OutlineVariantDark
    )
    AccentColor.ORANGE -> darkColorScheme(
        primary = PrimaryOrangeDark,
        onPrimary = OnPrimaryContainerOrange,
        primaryContainer = OnPrimaryContainerOrange,
        onPrimaryContainer = PrimaryOrangeDark,
        secondary = SecondaryDark,
        onSecondary = OnSecondaryDark,
        secondaryContainer = SecondaryContainerDark,
        onSecondaryContainer = SecondaryDark,
        tertiary = TertiaryDark,
        onTertiary = OnTertiaryDark,
        tertiaryContainer = TertiaryContainerDark,
        onTertiaryContainer = TertiaryDark,
        background = BackgroundDark,
        onBackground = OnBackgroundDark,
        surface = SurfaceDark,
        onSurface = OnSurfaceDark,
        surfaceVariant = SurfaceVariantDark,
        onSurfaceVariant = OnSurfaceVariantDark,
        error = ErrorDark,
        onError = OnErrorDark,
        errorContainer = ErrorContainerDark,
        onErrorContainer = ErrorDark,
        outline = OutlineDark,
        outlineVariant = OutlineVariantDark
    )
}

// Composition Local for font size preference
val LocalFontSizePreference = staticCompositionLocalOf { FontSizePreference.MEDIUM }

@Composable
fun QuoteVaultTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    accentColor: AccentColor = AccentColor.DEFAULT,
    fontSizePreference: FontSizePreference = FontSizePreference.MEDIUM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (darkTheme) {
        darkColorScheme(accentColor)
    } else {
        lightColorScheme(accentColor)
    }

    val typography = createTypography(fontSizePreference)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalFontSizePreference provides fontSizePreference
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = QuoteVaultShapes,
            content = content
        )
    }
}
