package com.example.quotevault.data.repository

import com.example.quotevault.core.common.Constants
import com.example.quotevault.data.local.database.dao.CollectionDao
import com.example.quotevault.data.local.database.dao.FavoriteDao
import com.example.quotevault.data.local.database.entity.CollectionEntity
import com.example.quotevault.data.local.database.entity.CollectionQuoteEntity
import com.example.quotevault.data.mapper.toDomain
import com.example.quotevault.data.mapper.toEntity
import com.example.quotevault.data.remote.dto.CollectionDto
import com.example.quotevault.data.remote.dto.CollectionInsertDto
import com.example.quotevault.data.remote.dto.CollectionQuoteDto
import com.example.quotevault.data.remote.dto.CollectionQuoteInsertDto
import com.example.quotevault.data.remote.dto.CollectionUpdateDto
import com.example.quotevault.domain.model.Collection
import com.example.quotevault.domain.model.Quote
import com.example.quotevault.domain.repository.CollectionRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val collectionDao: CollectionDao,
    private val favoriteDao: FavoriteDao
) : CollectionRepository {

    private val userId: String?
        get() = supabaseClient.auth.currentUserOrNull()?.id

    override fun getCollections(): Flow<List<Collection>> {
        val uid = userId ?: return flowOf(emptyList())
        return collectionDao.getCollections(uid).map { collections ->
            collections.map { entity ->
                val quoteCount = collectionDao.getQuoteCountInCollectionSync(entity.id)
                entity.toDomain(quoteCount)
            }
        }
    }

    override suspend fun getCollectionById(id: String): Collection? {
        return collectionDao.getCollectionById(id)?.let { entity ->
            val quoteCount = collectionDao.getQuoteCountInCollectionSync(entity.id)
            entity.toDomain(quoteCount)
        }
    }

    override fun observeCollectionById(id: String): Flow<Collection?> {
        return combine(
            collectionDao.observeCollectionById(id),
            collectionDao.getQuoteCountInCollection(id)
        ) { entity, count ->
            entity?.toDomain(count)
        }
    }

    override fun getQuotesInCollection(collectionId: String): Flow<List<Quote>> {
        val uid = userId
        return if (uid != null) {
            combine(
                collectionDao.getQuotesInCollection(collectionId),
                favoriteDao.getFavoriteQuoteIds(uid)
            ) { quotes, favoriteIds ->
                quotes.map { it.toDomain(favoriteIds.contains(it.id)) }
            }
        } else {
            collectionDao.getQuotesInCollection(collectionId).map { quotes ->
                quotes.map { it.toDomain(false) }
            }
        }
    }

    override suspend fun getQuotesInCollectionPaginated(collectionId: String, page: Int, pageSize: Int): List<Quote> {
        val uid = userId
        val offset = page * pageSize
        val favoriteIds = if (uid != null) {
            favoriteDao.getFavoriteQuoteIds(uid).first().toSet()
        } else emptySet()

        return collectionDao.getQuotesInCollectionPaginated(collectionId, pageSize, offset)
            .map { it.toDomain(favoriteIds.contains(it.id)) }
    }

    override fun isQuoteInCollection(collectionId: String, quoteId: String): Flow<Boolean> {
        return collectionDao.isQuoteInCollection(collectionId, quoteId)
    }

    override fun getCollectionIdsForQuote(quoteId: String): Flow<List<String>> {
        return collectionDao.getCollectionIdsForQuote(quoteId)
    }

    override suspend fun createCollection(name: String, description: String?, coverColor: String): Result<Collection> {
        val uid = userId ?: return Result.failure(Exception("Not logged in"))

        return try {
            val id = UUID.randomUUID().toString()
            val now = Clock.System.now()

            // Save locally
            val entity = CollectionEntity(
                id = id,
                userId = uid,
                name = name,
                description = description,
                coverColor = coverColor,
                createdAt = now,
                updatedAt = now
            )
            collectionDao.insertCollection(entity)

            // Sync to remote
            try {
                supabaseClient.postgrest[Constants.Tables.COLLECTIONS]
                    .insert(CollectionInsertDto(
                        userId = uid,
                        name = name,
                        description = description,
                        coverColor = coverColor
                    ))
            } catch (e: Exception) {
                // Ignore remote errors
            }

            Result.success(entity.toDomain(0))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCollection(id: String, name: String, description: String?, coverColor: String): Result<Collection> {
        return try {
            collectionDao.updateCollection(id, name, description, coverColor)

            // Sync to remote
            try {
                supabaseClient.postgrest[Constants.Tables.COLLECTIONS]
                    .update(CollectionUpdateDto(name = name, description = description, coverColor = coverColor)) {
                        filter { eq("id", id) }
                    }
            } catch (e: Exception) {
                // Ignore remote errors
            }

            val collection = getCollectionById(id) ?: return Result.failure(Exception("Collection not found"))
            Result.success(collection)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCollection(id: String): Result<Unit> {
        return try {
            collectionDao.removeAllQuotesFromCollection(id)
            collectionDao.deleteCollection(id)

            // Sync to remote
            try {
                supabaseClient.postgrest[Constants.Tables.COLLECTIONS]
                    .delete { filter { eq("id", id) } }
            } catch (e: Exception) {
                // Ignore remote errors
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addQuoteToCollection(collectionId: String, quoteId: String): Result<Unit> {
        return try {
            val entity = CollectionQuoteEntity(
                id = UUID.randomUUID().toString(),
                collectionId = collectionId,
                quoteId = quoteId,
                addedAt = Clock.System.now()
            )
            collectionDao.insertCollectionQuote(entity)

            // Sync to remote
            try {
                supabaseClient.postgrest[Constants.Tables.COLLECTION_QUOTES]
                    .insert(CollectionQuoteInsertDto(collectionId = collectionId, quoteId = quoteId))
            } catch (e: Exception) {
                // Ignore remote errors
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeQuoteFromCollection(collectionId: String, quoteId: String): Result<Unit> {
        return try {
            collectionDao.removeQuoteFromCollection(collectionId, quoteId)

            // Sync to remote
            try {
                supabaseClient.postgrest[Constants.Tables.COLLECTION_QUOTES]
                    .delete {
                        filter {
                            eq("collection_id", collectionId)
                            eq("quote_id", quoteId)
                        }
                    }
            } catch (e: Exception) {
                // Ignore remote errors
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncCollections(): Result<Unit> {
        val uid = userId ?: return Result.failure(Exception("Not logged in"))

        return try {
            val remoteCollections = supabaseClient.postgrest[Constants.Tables.COLLECTIONS]
                .select { filter { eq("user_id", uid) } }
                .decodeList<CollectionDto>()

            collectionDao.deleteAllCollections(uid)
            collectionDao.insertCollections(remoteCollections.map { it.toEntity() })

            // Sync collection quotes
            for (collection in remoteCollections) {
                val quotes = supabaseClient.postgrest[Constants.Tables.COLLECTION_QUOTES]
                    .select { filter { eq("collection_id", collection.id) } }
                    .decodeList<CollectionQuoteDto>()

                collectionDao.removeAllQuotesFromCollection(collection.id)
                collectionDao.insertCollectionQuotes(quotes.map { dto ->
                    CollectionQuoteEntity(
                        id = dto.id,
                        collectionId = dto.collectionId,
                        quoteId = dto.quoteId,
                        addedAt = dto.addedAt
                    )
                })
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
