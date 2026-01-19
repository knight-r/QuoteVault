package com.example.quotevault.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val QuoteVaultShapes = Shapes(
    // Extra small - for small chips, badges
    extraSmall = RoundedCornerShape(4.dp),

    // Small - for buttons, text fields
    small = RoundedCornerShape(8.dp),

    // Medium - for cards, dialogs
    medium = RoundedCornerShape(12.dp),

    // Large - for bottom sheets, navigation bars
    large = RoundedCornerShape(16.dp),

    // Extra large - for full-screen elements
    extraLarge = RoundedCornerShape(24.dp)
)

// Custom shapes for specific components
object CustomShapes {
    val QuoteCard = RoundedCornerShape(16.dp)
    val CategoryChip = RoundedCornerShape(20.dp)
    val Button = RoundedCornerShape(12.dp)
    val TextField = RoundedCornerShape(12.dp)
    val BottomSheet = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    val Dialog = RoundedCornerShape(24.dp)
    val Avatar = RoundedCornerShape(50)
    val ShareCard = RoundedCornerShape(20.dp)
    val Widget = RoundedCornerShape(24.dp)
}
