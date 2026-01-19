package com.example.quotevault.domain.repository

import com.example.quotevault.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavoriteQuotes(): Flow<List<Quote>>
    suspend fun getFavoriteQuotesPaginated(page: Int, pageSize: Int): List<Quote>
    fun isFavorite(quoteId: String): Flow<Boolean>
    suspend fun isFavoriteSync(quoteId: String): Boolean
    fun getFavoriteQuoteIds(): Flow<List<String>>
    suspend fun addFavorite(quoteId: String): Result<Unit>
    suspend fun removeFavorite(quoteId: String): Result<Unit>
    suspend fun toggleFavorite(quoteId: String): Result<Boolean>
    suspend fun syncFavorites(): Result<Unit>
}
