package com.example.quotevault.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "favorites",
    indices = [
        Index(value = ["user_id", "quote_id"], unique = true),
        Index(value = ["quote_id"])
    ]
)
data class FavoriteEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "quote_id")
    val quoteId: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Instant? = null
)
