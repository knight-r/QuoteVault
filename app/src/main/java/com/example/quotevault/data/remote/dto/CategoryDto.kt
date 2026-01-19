package com.example.quotevault.data.remote.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("display_name")
    val displayName: String,

    @SerialName("icon_name")
    val iconName: String? = null,

    @SerialName("color_hex")
    val colorHex: String? = null,

    @SerialName("sort_order")
    val sortOrder: Int = 0,

    @SerialName("created_at")
    val createdAt: Instant? = null
)
