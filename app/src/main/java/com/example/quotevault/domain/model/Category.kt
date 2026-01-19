package com.example.quotevault.domain.model

import androidx.compose.ui.graphics.Color
import com.example.quotevault.core.ui.theme.CategoryHumor
import com.example.quotevault.core.ui.theme.CategoryLove
import com.example.quotevault.core.ui.theme.CategoryMotivation
import com.example.quotevault.core.ui.theme.CategorySuccess
import com.example.quotevault.core.ui.theme.CategoryWisdom

data class Category(
    val id: String,
    val name: String,
    val displayName: String,
    val iconName: String? = null,
    val colorHex: String? = null,
    val sortOrder: Int = 0,
    val quoteCount: Int = 0
) {
    val color: Color
        get() = when (name.lowercase()) {
            "motivation" -> CategoryMotivation
            "love" -> CategoryLove
            "success" -> CategorySuccess
            "wisdom" -> CategoryWisdom
            "humor" -> CategoryHumor
            else -> CategoryWisdom
        }

    val icon: String
        get() = iconName ?: when (name.lowercase()) {
            "motivation" -> "🔥"
            "love" -> "❤️"
            "success" -> "🏆"
            "wisdom" -> "🧠"
            "humor" -> "😄"
            else -> "💡"
        }
}
