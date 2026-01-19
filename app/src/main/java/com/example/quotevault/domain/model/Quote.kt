package com.example.quotevault.domain.model

import kotlinx.datetime.Instant

data class Quote(
    val id: String,
    val text: String,
    val author: String,
    val authorImageUrl: String? = null,
    val categoryId: String? = null,
    val categoryName: String? = null,
    val source: String? = null,
    val isFeatured: Boolean = false,
    val isFavorite: Boolean = false,
    val createdAt: Instant? = null
) {
    val formattedQuote: String
        get() = "\"$text\""

    val shareText: String
        get() = "\"$text\"\n\n— $author"
}
