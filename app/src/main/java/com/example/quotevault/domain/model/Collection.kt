package com.example.quotevault.domain.model

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.Instant

data class Collection(
    val id: String,
    val userId: String,
    val name: String,
    val description: String? = null,
    val coverColor: String = "#6366F1",
    val isPublic: Boolean = false,
    val quoteCount: Int = 0,
    val quotes: List<Quote> = emptyList(),
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
) {
    val color: Color
        get() = try {
            Color(android.graphics.Color.parseColor(coverColor))
        } catch (e: Exception) {
            Color(0xFF6366F1)
        }
}
