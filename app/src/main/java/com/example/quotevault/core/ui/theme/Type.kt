package com.example.quotevault.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.quotevault.R

// Custom Font Family - Using default system fonts with fallback
val QuoteFont = FontFamily.Default

// Quote Display Font - For prominent quote text
val QuoteDisplayFont = FontFamily.Serif

// Font size multipliers for different settings
enum class FontSizePreference(val multiplier: Float) {
    SMALL(0.85f),
    MEDIUM(1.0f),
    LARGE(1.15f),
    EXTRA_LARGE(1.3f)
}

fun createTypography(fontSizePreference: FontSizePreference = FontSizePreference.MEDIUM): Typography {
    val multiplier = fontSizePreference.multiplier

    return Typography(
        // Display styles - for very large text
        displayLarge = TextStyle(
            fontFamily = QuoteDisplayFont,
            fontWeight = FontWeight.Normal,
            fontSize = (57 * multiplier).sp,
            lineHeight = (64 * multiplier).sp,
            letterSpacing = (-0.25).sp
        ),
        displayMedium = TextStyle(
            fontFamily = QuoteDisplayFont,
            fontWeight = FontWeight.Normal,
            fontSize = (45 * multiplier).sp,
            lineHeight = (52 * multiplier).sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = QuoteDisplayFont,
            fontWeight = FontWeight.Normal,
            fontSize = (36 * multiplier).sp,
            lineHeight = (44 * multiplier).sp,
            letterSpacing = 0.sp
        ),

        // Headline styles - for section headers
        headlineLarge = TextStyle(
            fontFamily = QuoteFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = (32 * multiplier).sp,
            lineHeight = (40 * multiplier).sp,
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = QuoteFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = (28 * multiplier).sp,
            lineHeight = (36 * multiplier).sp,
            letterSpacing = 0.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = QuoteFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = (24 * multiplier).sp,
            lineHeight = (32 * multiplier).sp,
            letterSpacing = 0.sp
        ),

        // Title styles - for card titles, list items
        titleLarge = TextStyle(
            fontFamily = QuoteFont,
            fontWeight = FontWeight.Medium,
            fontSize = (22 * multiplier).sp,
            lineHeight = (28 * multiplier).sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = QuoteFont,
            fontWeight = FontWeight.Medium,
            fontSize = (16 * multiplier).sp,
            lineHeight = (24 * multiplier).sp,
            letterSpacing = 0.15.sp
        ),
        titleSmall = TextStyle(
            fontFamily = QuoteFont,
            fontWeight = FontWeight.Medium,
            fontSize = (14 * multiplier).sp,
            lineHeight = (20 * multiplier).sp,
            letterSpacing = 0.1.sp
        ),

        // Body styles - for main content
        bodyLarge = TextStyle(
            fontFamily = QuoteFont,
            fontWeight = FontWeight.Normal,
            fontSize = (16 * multiplier).sp,
            lineHeight = (24 * multiplier).sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = QuoteFont,
            fontWeight = FontWeight.Normal,
            fontSize = (14 * multiplier).sp,
            lineHeight = (20 * multiplier).sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontFamily = QuoteFont,
            fontWeight = FontWeight.Normal,
            fontSize = (12 * multiplier).sp,
            lineHeight = (16 * multiplier).sp,
            letterSpacing = 0.4.sp
        ),

        // Label styles - for buttons, captions
        labelLarge = TextStyle(
            fontFamily = QuoteFont,
            fontWeight = FontWeight.Medium,
            fontSize = (14 * multiplier).sp,
            lineHeight = (20 * multiplier).sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = QuoteFont,
            fontWeight = FontWeight.Medium,
            fontSize = (12 * multiplier).sp,
            lineHeight = (16 * multiplier).sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = QuoteFont,
            fontWeight = FontWeight.Medium,
            fontSize = (11 * multiplier).sp,
            lineHeight = (16 * multiplier).sp,
            letterSpacing = 0.5.sp
        )
    )
}

// Quote-specific text styles
object QuoteTextStyles {
    fun quoteText(fontSizePreference: FontSizePreference = FontSizePreference.MEDIUM) = TextStyle(
        fontFamily = QuoteDisplayFont,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = (20 * fontSizePreference.multiplier).sp,
        lineHeight = (28 * fontSizePreference.multiplier).sp,
        letterSpacing = 0.sp
    )

    fun quoteTextLarge(fontSizePreference: FontSizePreference = FontSizePreference.MEDIUM) = TextStyle(
        fontFamily = QuoteDisplayFont,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = (24 * fontSizePreference.multiplier).sp,
        lineHeight = (32 * fontSizePreference.multiplier).sp,
        letterSpacing = 0.sp
    )

    fun authorText(fontSizePreference: FontSizePreference = FontSizePreference.MEDIUM) = TextStyle(
        fontFamily = QuoteFont,
        fontWeight = FontWeight.Medium,
        fontSize = (14 * fontSizePreference.multiplier).sp,
        lineHeight = (20 * fontSizePreference.multiplier).sp,
        letterSpacing = 0.25.sp
    )
}
