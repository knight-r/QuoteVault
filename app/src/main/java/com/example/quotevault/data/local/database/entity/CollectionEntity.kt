package com.example.quotevault.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "collections",
    indices = [Index(value = ["user_id"])]
)
data class CollectionEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "cover_color")
    val coverColor: String = "#6366F1",

    @ColumnInfo(name = "is_public")
    val isPublic: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Instant? = null,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Instant? = null
)

@Entity(
    tableName = "collection_quotes",
    indices = [
        Index(value = ["collection_id", "quote_id"], unique = true),
        Index(value = ["quote_id"])
    ]
)
data class CollectionQuoteEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "collection_id")
    val collectionId: String,

    @ColumnInfo(name = "quote_id")
    val quoteId: String,

    @ColumnInfo(name = "added_at")
    val addedAt: Instant? = null
)
