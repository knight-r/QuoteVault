package com.example.quotevault.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.quotevault.data.local.database.entity.CollectionEntity
import com.example.quotevault.data.local.database.entity.CollectionQuoteEntity
import com.example.quotevault.data.local.database.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Query("SELECT * FROM collections WHERE user_id = :userId ORDER BY created_at DESC")
    fun getCollections(userId: String): Flow<List<CollectionEntity>>

    @Query("SELECT * FROM collections WHERE user_id = :userId ORDER BY created_at DESC")
    suspend fun getCollectionsSync(userId: String): List<CollectionEntity>

    @Query("SELECT * FROM collections WHERE id = :id")
    suspend fun getCollectionById(id: String): CollectionEntity?

    @Query("SELECT * FROM collections WHERE id = :id")
    fun observeCollectionById(id: String): Flow<CollectionEntity?>

    @Query("""
        SELECT q.* FROM quotes q
        INNER JOIN collection_quotes cq ON q.id = cq.quote_id
        WHERE cq.collection_id = :collectionId
        ORDER BY cq.added_at DESC
    """)
    fun getQuotesInCollection(collectionId: String): Flow<List<QuoteEntity>>

    @Query("""
        SELECT q.* FROM quotes q
        INNER JOIN collection_quotes cq ON q.id = cq.quote_id
        WHERE cq.collection_id = :collectionId
        ORDER BY cq.added_at DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getQuotesInCollectionPaginated(collectionId: String, limit: Int, offset: Int): List<QuoteEntity>

    @Query("SELECT COUNT(*) FROM collection_quotes WHERE collection_id = :collectionId")
    fun getQuoteCountInCollection(collectionId: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM collection_quotes WHERE collection_id = :collectionId")
    suspend fun getQuoteCountInCollectionSync(collectionId: String): Int

    @Query("SELECT EXISTS(SELECT 1 FROM collection_quotes WHERE collection_id = :collectionId AND quote_id = :quoteId)")
    fun isQuoteInCollection(collectionId: String, quoteId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM collection_quotes WHERE collection_id = :collectionId AND quote_id = :quoteId)")
    suspend fun isQuoteInCollectionSync(collectionId: String, quoteId: String): Boolean

    @Query("SELECT collection_id FROM collection_quotes WHERE quote_id = :quoteId")
    fun getCollectionIdsForQuote(quoteId: String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollections(collections: List<CollectionEntity>)

    @Query("UPDATE collections SET name = :name, description = :description, cover_color = :coverColor WHERE id = :id")
    suspend fun updateCollection(id: String, name: String, description: String?, coverColor: String)

    @Query("DELETE FROM collections WHERE id = :id")
    suspend fun deleteCollection(id: String)

    @Query("DELETE FROM collections WHERE user_id = :userId")
    suspend fun deleteAllCollections(userId: String)

    // Collection Quotes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectionQuote(collectionQuote: CollectionQuoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectionQuotes(collectionQuotes: List<CollectionQuoteEntity>)

    @Query("DELETE FROM collection_quotes WHERE collection_id = :collectionId AND quote_id = :quoteId")
    suspend fun removeQuoteFromCollection(collectionId: String, quoteId: String)

    @Query("DELETE FROM collection_quotes WHERE collection_id = :collectionId")
    suspend fun removeAllQuotesFromCollection(collectionId: String)
}
