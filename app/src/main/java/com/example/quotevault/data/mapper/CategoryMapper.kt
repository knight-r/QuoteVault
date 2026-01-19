package com.example.quotevault.data.mapper

import com.example.quotevault.data.local.database.entity.CategoryEntity
import com.example.quotevault.data.remote.dto.CategoryDto
import com.example.quotevault.domain.model.Category

fun CategoryDto.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        displayName = displayName,
        iconName = iconName,
        colorHex = colorHex,
        sortOrder = sortOrder,
        createdAt = createdAt
    )
}

fun CategoryDto.toDomain(quoteCount: Int = 0): Category {
    return Category(
        id = id,
        name = name,
        displayName = displayName,
        iconName = iconName,
        colorHex = colorHex,
        sortOrder = sortOrder,
        quoteCount = quoteCount
    )
}

fun CategoryEntity.toDomain(quoteCount: Int = 0): Category {
    return Category(
        id = id,
        name = name,
        displayName = displayName,
        iconName = iconName,
        colorHex = colorHex,
        sortOrder = sortOrder,
        quoteCount = quoteCount
    )
}
