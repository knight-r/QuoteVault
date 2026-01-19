package com.example.quotevault.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quotevault.data.local.database.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Query("SELECT * FROM quotes ORDER BY created_at DESC")
    fun getAllQuotes(): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    suspend fun getQuotesPaginated(limit: Int, offset: Int): List<QuoteEntity>

    @Query("SELECT * FROM quotes WHERE category_id = :categoryId ORDER BY created_at DESC")
    fun getQuotesByCategory(categoryId: String): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE category_id = :categoryId ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    suspend fun getQuotesByCategoryPaginated(categoryId: String, limit: Int, offset: Int): List<QuoteEntity>

    @Query("""
        SELECT * FROM quotes
        WHERE text LIKE '%' || :query || '%'
        OR author LIKE '%' || :query || '%'
        ORDER BY created_at DESC
    """)
    fun searchQuotes(query: String): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE author LIKE '%' || :author || '%' ORDER BY created_at DESC")
    fun getQuotesByAuthor(author: String): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM quotes WHERE id = :id")
    suspend fun getQuoteById(id: String): QuoteEntity?

    @Query("SELECT * FROM quotes WHERE id = :id")
    fun observeQuoteById(id: String): Flow<QuoteEntity?>

    @Query("SELECT * FROM quotes WHERE is_featured = 1 LIMIT 1")
    suspend fun getFeaturedQuote(): QuoteEntity?

    @Query("SELECT * FROM quotes ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomQuote(): QuoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: List<QuoteEntity>)

    @Query("DELETE FROM quotes WHERE id = :id")
    suspend fun deleteQuote(id: String)

    @Query("DELETE FROM quotes")
    suspend fun deleteAllQuotes()

    @Query("SELECT COUNT(*) FROM quotes")
    suspend fun getQuoteCount(): Int

    @Query("SELECT COUNT(*) FROM quotes WHERE category_id = :categoryId")
    suspend fun getQuoteCountByCategory(categoryId: String): Int
}
