package com.example.quotevault.data.remote.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionDto(
    @SerialName("id")
    val id: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("cover_color")
    val coverColor: String = "#6366F1",

    @SerialName("is_public")
    val isPublic: Boolean = false,

    @SerialName("created_at")
    val createdAt: Instant? = null,

    @SerialName("updated_at")
    val updatedAt: Instant? = null
)

@Serializable
data class CollectionInsertDto(
    @SerialName("user_id")
    val userId: String,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String? = null,

    @SerialName("cover_color")
    val coverColor: String = "#6366F1",

    @SerialName("is_public")
    val isPublic: Boolean = false
)

@Serializable
data class CollectionUpdateDto(
    @SerialName("name")
    val name: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("cover_color")
    val coverColor: String? = null,

    @SerialName("is_public")
    val isPublic: Boolean? = null
)

@Serializable
data class CollectionQuoteDto(
    @SerialName("id")
    val id: String,

    @SerialName("collection_id")
    val collectionId: String,

    @SerialName("quote_id")
    val quoteId: String,

    @SerialName("added_at")
    val addedAt: Instant? = null,

    @SerialName("quotes")
    val quote: QuoteDto? = null
)

@Serializable
data class CollectionQuoteInsertDto(
    @SerialName("collection_id")
    val collectionId: String,

    @SerialName("quote_id")
    val quoteId: String
)
