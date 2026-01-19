package com.example.quotevault.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "quotes",
    indices = [
        Index(value = ["category_id"]),
        Index(value = ["author"])
    ]
)
data class QuoteEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "author")
    val author: String,

    @ColumnInfo(name = "author_image_url")
    val authorImageUrl: String? = null,

    @ColumnInfo(name = "category_id")
    val categoryId: String? = null,

    @ColumnInfo(name = "category_name")
    val categoryName: String? = null,

    @ColumnInfo(name = "source")
    val source: String? = null,

    @ColumnInfo(name = "is_featured")
    val isFeatured: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Instant? = null,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Instant? = null
)
