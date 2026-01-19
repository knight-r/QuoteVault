package com.example.quotevault.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.quotevault.data.local.database.entity.FavoriteEntity
import com.example.quotevault.data.local.database.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("""
        SELECT q.* FROM quotes q
        INNER JOIN favorites f ON q.id = f.quote_id
        WHERE f.user_id = :userId
        ORDER BY f.created_at DESC
    """)
    fun getFavoriteQuotes(userId: String): Flow<List<QuoteEntity>>

    @Query("""
        SELECT q.* FROM quotes q
        INNER JOIN favorites f ON q.id = f.quote_id
        WHERE f.user_id = :userId
        ORDER BY f.created_at DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getFavoriteQuotesPaginated(userId: String, limit: Int, offset: Int): List<QuoteEntity>

    @Query("SELECT * FROM favorites WHERE user_id = :userId")
    fun getAllFavorites(userId: String): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE user_id = :userId AND quote_id = :quoteId")
    suspend fun getFavorite(userId: String, quoteId: String): FavoriteEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE user_id = :userId AND quote_id = :quoteId)")
    fun isFavorite(userId: String, quoteId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE user_id = :userId AND quote_id = :quoteId)")
    suspend fun isFavoriteSync(userId: String, quoteId: String): Boolean

    @Query("SELECT quote_id FROM favorites WHERE user_id = :userId")
    fun getFavoriteQuoteIds(userId: String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(favorites: List<FavoriteEntity>)

    @Query("DELETE FROM favorites WHERE user_id = :userId AND quote_id = :quoteId")
    suspend fun deleteFavorite(userId: String, quoteId: String)

    @Query("DELETE FROM favorites WHERE user_id = :userId")
    suspend fun deleteAllFavorites(userId: String)

    @Query("SELECT COUNT(*) FROM favorites WHERE user_id = :userId")
    suspend fun getFavoriteCount(userId: String): Int
}
