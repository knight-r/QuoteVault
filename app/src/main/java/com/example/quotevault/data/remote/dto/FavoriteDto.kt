package com.example.quotevault.data.remote.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteDto(
    @SerialName("id")
    val id: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("quote_id")
    val quoteId: String,

    @SerialName("created_at")
    val createdAt: Instant? = null,

    @SerialName("quotes")
    val quote: QuoteDto? = null
)

@Serializable
data class FavoriteInsertDto(
    @SerialName("user_id")
    val userId: String,

    @SerialName("quote_id")
    val quoteId: String
)
