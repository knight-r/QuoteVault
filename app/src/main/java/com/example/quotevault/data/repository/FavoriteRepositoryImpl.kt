package com.example.quotevault.data.repository

import com.example.quotevault.core.common.Constants
import com.example.quotevault.data.local.database.dao.FavoriteDao
import com.example.quotevault.data.local.database.dao.QuoteDao
import com.example.quotevault.data.local.database.entity.FavoriteEntity
import com.example.quotevault.data.mapper.toDomain
import com.example.quotevault.data.remote.dto.FavoriteDto
import com.example.quotevault.data.remote.dto.FavoriteInsertDto
import com.example.quotevault.domain.model.Quote
import com.example.quotevault.domain.repository.FavoriteRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val favoriteDao: FavoriteDao,
    private val quoteDao: QuoteDao
) : FavoriteRepository {

    private val userId: String?
        get() = supabaseClient.auth.currentUserOrNull()?.id

    override fun getFavoriteQuotes(): Flow<List<Quote>> {
        val uid = userId ?: return flowOf(emptyList())
        return favoriteDao.getFavoriteQuotes(uid).map { quotes ->
            quotes.map { it.toDomain(isFavorite = true) }
        }
    }

    override suspend fun getFavoriteQuotesPaginated(page: Int, pageSize: Int): List<Quote> {
        val uid = userId ?: return emptyList()
        val offset = page * pageSize
        return favoriteDao.getFavoriteQuotesPaginated(uid, pageSize, offset)
            .map { it.toDomain(isFavorite = true) }
    }

    override fun isFavorite(quoteId: String): Flow<Boolean> {
        val uid = userId ?: return flowOf(false)
        return favoriteDao.isFavorite(uid, quoteId)
    }

    override suspend fun isFavoriteSync(quoteId: String): Boolean {
        val uid = userId ?: return false
        return favoriteDao.isFavoriteSync(uid, quoteId)
    }

    override fun getFavoriteQuoteIds(): Flow<List<String>> {
        val uid = userId ?: return flowOf(emptyList())
        return favoriteDao.getFavoriteQuoteIds(uid)
    }

    override suspend fun addFavorite(quoteId: String): Result<Unit> {
        val uid = userId ?: return Result.failure(Exception("Not logged in"))

        return try {
            // Add locally
            val favoriteEntity = FavoriteEntity(
                id = UUID.randomUUID().toString(),
                userId = uid,
                quoteId = quoteId,
                createdAt = Clock.System.now()
            )
            favoriteDao.insertFavorite(favoriteEntity)

            // Sync to remote
            try {
                supabaseClient.postgrest[Constants.Tables.USER_FAVORITES]
                    .insert(FavoriteInsertDto(userId = uid, quoteId = quoteId))
            } catch (e: Exception) {
                // Ignore remote errors, local is saved
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFavorite(quoteId: String): Result<Unit> {
        val uid = userId ?: return Result.failure(Exception("Not logged in"))

        return try {
            // Remove locally
            favoriteDao.deleteFavorite(uid, quoteId)

            // Sync to remote
            try {
                supabaseClient.postgrest[Constants.Tables.USER_FAVORITES]
                    .delete {
                        filter {
                            eq("user_id", uid)
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

    override suspend fun toggleFavorite(quoteId: String): Result<Boolean> {
        val isFav = isFavoriteSync(quoteId)
        return if (isFav) {
            removeFavorite(quoteId).map { false }
        } else {
            addFavorite(quoteId).map { true }
        }
    }

    override suspend fun syncFavorites(): Result<Unit> {
        val uid = userId ?: return Result.failure(Exception("Not logged in"))

        return try {
            val remoteFavorites = supabaseClient.postgrest[Constants.Tables.USER_FAVORITES]
                .select { filter { eq("user_id", uid) } }
                .decodeList<FavoriteDto>()

            // Clear local and replace with remote
            favoriteDao.deleteAllFavorites(uid)
            favoriteDao.insertFavorites(remoteFavorites.map { dto ->
                FavoriteEntity(
                    id = dto.id,
                    userId = dto.userId,
                    quoteId = dto.quoteId,
                    createdAt = dto.createdAt
                )
            })

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
