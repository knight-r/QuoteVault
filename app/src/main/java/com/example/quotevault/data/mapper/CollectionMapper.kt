package com.example.quotevault.data.mapper

import com.example.quotevault.data.local.database.entity.CollectionEntity
import com.example.quotevault.data.remote.dto.CollectionDto
import com.example.quotevault.domain.model.Collection
import com.example.quotevault.domain.model.Quote

fun CollectionDto.toEntity(): CollectionEntity {
    return CollectionEntity(
        id = id,
        userId = userId,
        name = name,
        description = description,
        coverColor = coverColor,
        isPublic = isPublic,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun CollectionDto.toDomain(quoteCount: Int = 0, quotes: List<Quote> = emptyList()): Collection {
    return Collection(
        id = id,
        userId = userId,
        name = name,
        description = description,
        coverColor = coverColor,
        isPublic = isPublic,
        quoteCount = quoteCount,
        quotes = quotes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun CollectionEntity.toDomain(quoteCount: Int = 0, quotes: List<Quote> = emptyList()): Collection {
    return Collection(
        id = id,
        userId = userId,
        name = name,
        description = description,
        coverColor = coverColor,
        isPublic = isPublic,
        quoteCount = quoteCount,
        quotes = quotes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Collection.toEntity(): CollectionEntity {
    return CollectionEntity(
        id = id,
        userId = userId,
        name = name,
        description = description,
        coverColor = coverColor,
        isPublic = isPublic,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
