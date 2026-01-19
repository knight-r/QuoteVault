package com.example.quotevault.domain.repository

import com.example.quotevault.domain.model.Collection
import com.example.quotevault.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun getCollections(): Flow<List<Collection>>
    suspend fun getCollectionById(id: String): Collection?
    fun observeCollectionById(id: String): Flow<Collection?>
    fun getQuotesInCollection(collectionId: String): Flow<List<Quote>>
    suspend fun getQuotesInCollectionPaginated(collectionId: String, page: Int, pageSize: Int): List<Quote>
    fun isQuoteInCollection(collectionId: String, quoteId: String): Flow<Boolean>
    fun getCollectionIdsForQuote(quoteId: String): Flow<List<String>>

    suspend fun createCollection(name: String, description: String?, coverColor: String): Result<Collection>
    suspend fun updateCollection(id: String, name: String, description: String?, coverColor: String): Result<Collection>
    suspend fun deleteCollection(id: String): Result<Unit>
    suspend fun addQuoteToCollection(collectionId: String, quoteId: String): Result<Unit>
    suspend fun removeQuoteFromCollection(collectionId: String, quoteId: String): Result<Unit>
    suspend fun syncCollections(): Result<Unit>
}
