package com.example.quotevault.data.remote.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteDto(
    @SerialName("id")
    val id: String,

    @SerialName("text")
    val text: String,

    @SerialName("author")
    val author: String,

    @SerialName("author_image_url")
    val authorImageUrl: String? = null,

    @SerialName("category_id")
    val categoryId: String? = null,

    @SerialName("source")
    val source: String? = null,

    @SerialName("is_featured")
    val isFeatured: Boolean = false,

    @SerialName("created_at")
    val createdAt: Instant? = null,

    @SerialName("updated_at")
    val updatedAt: Instant? = null,

    // Joined fields
    @SerialName("categories")
    val category: CategoryDto? = null
)

@Serializable
data class QuoteOfDayDto(
    @SerialName("id")
    val id: String,

    @SerialName("quote_id")
    val quoteId: String,

    @SerialName("display_date")
    val displayDate: String,

    @SerialName("created_at")
    val createdAt: Instant? = null,

    @SerialName("quotes")
    val quote: QuoteDto? = null
)
