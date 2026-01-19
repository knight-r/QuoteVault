package com.example.quotevault.data.repository

import com.example.quotevault.core.common.Constants
import com.example.quotevault.data.local.database.dao.CategoryDao
import com.example.quotevault.data.local.database.dao.FavoriteDao
import com.example.quotevault.data.local.database.dao.QuoteDao
import com.example.quotevault.data.mapper.toDomain
import com.example.quotevault.data.mapper.toEntity
import com.example.quotevault.data.remote.dto.CategoryDto
import com.example.quotevault.data.remote.dto.QuoteDto
import com.example.quotevault.data.remote.dto.QuoteOfDayDto
import com.example.quotevault.domain.model.Category
import com.example.quotevault.domain.model.Quote
import com.example.quotevault.domain.repository.QuoteRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val quoteDao: QuoteDao,
    private val categoryDao: CategoryDao,
    private val favoriteDao: FavoriteDao
) : QuoteRepository {

    private val userId: String?
        get() = supabaseClient.auth.currentUserOrNull()?.id

    override fun getQuotes(): Flow<List<Quote>> {
        return combine(
            quoteDao.getAllQuotes(),
            getFavoriteIdsFlow()
        ) { quotes, favoriteIds ->
            quotes.map { it.toDomain(favoriteIds.contains(it.id)) }
        }
    }

    override suspend fun getQuotesPaginated(page: Int, pageSize: Int): List<Quote> {
        val offset = page * pageSize
        val favoriteIds = getFavoriteIds()
        return quoteDao.getQuotesPaginated(pageSize, offset)
            .map { it.toDomain(favoriteIds.contains(it.id)) }
    }

    override fun getQuotesByCategory(categoryId: String): Flow<List<Quote>> {
        return combine(
            quoteDao.getQuotesByCategory(categoryId),
            getFavoriteIdsFlow()
        ) { quotes, favoriteIds ->
            quotes.map { it.toDomain(favoriteIds.contains(it.id)) }
        }
    }

    override suspend fun getQuotesByCategoryPaginated(categoryId: String, page: Int, pageSize: Int): List<Quote> {
        val offset = page * pageSize
        val favoriteIds = getFavoriteIds()
        return quoteDao.getQuotesByCategoryPaginated(categoryId, pageSize, offset)
            .map { it.toDomain(favoriteIds.contains(it.id)) }
    }

    override fun searchQuotes(query: String): Flow<List<Quote>> {
        return combine(
            quoteDao.searchQuotes(query),
            getFavoriteIdsFlow()
        ) { quotes, favoriteIds ->
            quotes.map { it.toDomain(favoriteIds.contains(it.id)) }
        }
    }

    override fun getQuotesByAuthor(author: String): Flow<List<Quote>> {
        return combine(
            quoteDao.getQuotesByAuthor(author),
            getFavoriteIdsFlow()
        ) { quotes, favoriteIds ->
            quotes.map { it.toDomain(favoriteIds.contains(it.id)) }
        }
    }

    override suspend fun getQuoteById(id: String): Quote? {
        val favoriteIds = getFavoriteIds()
        return quoteDao.getQuoteById(id)?.toDomain(favoriteIds.contains(id))
    }

    override fun observeQuoteById(id: String): Flow<Quote?> {
        return combine(
            quoteDao.observeQuoteById(id),
            getFavoriteIdsFlow()
        ) { quote, favoriteIds ->
            quote?.toDomain(favoriteIds.contains(id))
        }
    }

    override suspend fun getQuoteOfDay(): Quote? {
        return try {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            val qod = supabaseClient.postgrest[Constants.Tables.QUOTE_OF_DAY]
                .select(Columns.raw("*, quotes(*, categories(*))")) {
                    filter { eq("display_date", today) }
                }
                .decodeSingleOrNull<QuoteOfDayDto>()

            val favoriteIds = getFavoriteIds()
            qod?.quote?.toDomain(favoriteIds.contains(qod.quote.id))
                ?: quoteDao.getRandomQuote()?.let { entity -> entity.toDomain(favoriteIds.contains(entity.id)) }
        } catch (e: Exception) {
            val favoriteIds = getFavoriteIds()
            quoteDao.getRandomQuote()?.let { entity -> entity.toDomain(favoriteIds.contains(entity.id)) }
        }
    }

    override suspend fun getRandomQuote(): Quote? {
        val favoriteIds = getFavoriteIds()
        return quoteDao.getRandomQuote()?.let { entity -> entity.toDomain(favoriteIds.contains(entity.id)) }
    }

    override suspend fun refreshQuotes(): Result<Unit> {
        return try {
            val quotes = supabaseClient.postgrest[Constants.Tables.QUOTES]
                .select(Columns.raw("*, categories(*)")) {
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<QuoteDto>()

            quoteDao.insertQuotes(quotes.map { it.toEntity() })
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { categories ->
            categories.map { entity ->
                val count = quoteDao.getQuoteCountByCategory(entity.id)
                entity.toDomain(count)
            }
        }
    }

    override suspend fun getCategoryById(id: String): Category? {
        return categoryDao.getCategoryById(id)?.let { entity ->
            val count = quoteDao.getQuoteCountByCategory(entity.id)
            entity.toDomain(count)
        }
    }

    override suspend fun refreshCategories(): Result<Unit> {
        return try {
            val categories = supabaseClient.postgrest[Constants.Tables.CATEGORIES]
                .select {
                    order("sort_order", Order.ASCENDING)
                }
                .decodeList<CategoryDto>()

            categoryDao.insertCategories(categories.map { it.toEntity() })
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getFavoriteIdsFlow(): Flow<Set<String>> {
        val uid = userId
        return if (uid != null) {
            favoriteDao.getFavoriteQuoteIds(uid).map { it.toSet() }
        } else {
            kotlinx.coroutines.flow.flowOf(emptySet())
        }
    }

    private suspend fun getFavoriteIds(): Set<String> {
        val uid = userId ?: return emptySet()
        return favoriteDao.getFavoriteQuoteIds(uid).first().toSet()
    }
}
