package com.example.quotevault.domain.repository

import com.example.quotevault.domain.model.Category
import com.example.quotevault.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    fun getQuotes(): Flow<List<Quote>>
    suspend fun getQuotesPaginated(page: Int, pageSize: Int): List<Quote>
    fun getQuotesByCategory(categoryId: String): Flow<List<Quote>>
    suspend fun getQuotesByCategoryPaginated(categoryId: String, page: Int, pageSize: Int): List<Quote>
    fun searchQuotes(query: String): Flow<List<Quote>>
    fun getQuotesByAuthor(author: String): Flow<List<Quote>>
    suspend fun getQuoteById(id: String): Quote?
    fun observeQuoteById(id: String): Flow<Quote?>
    suspend fun getQuoteOfDay(): Quote?
    suspend fun getRandomQuote(): Quote?
    suspend fun refreshQuotes(): Result<Unit>

    // Categories
    fun getCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: String): Category?
    suspend fun refreshCategories(): Result<Unit>
}
