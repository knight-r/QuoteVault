package com.example.quotevault.data.mapper

import com.example.quotevault.data.local.database.entity.QuoteEntity
import com.example.quotevault.data.remote.dto.QuoteDto
import com.example.quotevault.domain.model.Quote

fun QuoteDto.toEntity(): QuoteEntity {
    return QuoteEntity(
        id = id,
        text = text,
        author = author,
        authorImageUrl = authorImageUrl,
        categoryId = categoryId,
        categoryName = category?.displayName,
        source = source,
        isFeatured = isFeatured,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun QuoteDto.toDomain(isFavorite: Boolean = false): Quote {
    return Quote(
        id = id,
        text = text,
        author = author,
        authorImageUrl = authorImageUrl,
        categoryId = categoryId,
        categoryName = category?.displayName,
        source = source,
        isFeatured = isFeatured,
        isFavorite = isFavorite,
        createdAt = createdAt
    )
}

fun QuoteEntity.toDomain(isFavorite: Boolean = false): Quote {
    return Quote(
        id = id,
        text = text,
        author = author,
        authorImageUrl = authorImageUrl,
        categoryId = categoryId,
        categoryName = categoryName,
        source = source,
        isFeatured = isFeatured,
        isFavorite = isFavorite,
        createdAt = createdAt
    )
}

fun Quote.toEntity(): QuoteEntity {
    return QuoteEntity(
        id = id,
        text = text,
        author = author,
        authorImageUrl = authorImageUrl,
        categoryId = categoryId,
        categoryName = categoryName,
        source = source,
        isFeatured = isFeatured,
        createdAt = createdAt
    )
}
