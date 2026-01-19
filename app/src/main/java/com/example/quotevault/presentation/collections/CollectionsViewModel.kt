package com.example.quotevault.presentation.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotevault.domain.model.Collection
import com.example.quotevault.domain.model.Quote
import com.example.quotevault.domain.repository.AuthRepository
import com.example.quotevault.domain.repository.CollectionRepository
import com.example.quotevault.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CollectionsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val collections: List<Collection> = emptyList(),
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val showCreateDialog: Boolean = false,
    val isCreating: Boolean = false
)

data class CollectionDetailUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val collection: Collection? = null,
    val quotes: List<Quote> = emptyList(),
    val error: String? = null,
    val showEditDialog: Boolean = false,
    val showDeleteDialog: Boolean = false
)

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val favoriteRepository: FavoriteRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionsUiState())
    val uiState: StateFlow<CollectionsUiState> = _uiState.asStateFlow()

    private val _detailState = MutableStateFlow(CollectionDetailUiState())
    val detailState: StateFlow<CollectionDetailUiState> = _detailState.asStateFlow()

    init {
        observeAuthState()
        observeCollections()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.isLoggedIn.collect { isLoggedIn ->
                _uiState.update { it.copy(isLoggedIn = isLoggedIn) }
                if (isLoggedIn) {
                    syncCollections()
                }
            }
        }
    }

    private fun observeCollections() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            collectionRepository.getCollections().collect { collections ->
                _uiState.update { it.copy(isLoading = false, collections = collections) }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            try {
                collectionRepository.syncCollections()
                _uiState.update { it.copy(isRefreshing = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isRefreshing = false, error = e.message) }
            }
        }
    }

    private fun syncCollections() {
        viewModelScope.launch {
            try {
                collectionRepository.syncCollections()
            } catch (e: Exception) {
                // Silently fail
            }
        }
    }

    fun showCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = true) }
    }

    fun hideCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = false) }
    }

    fun createCollection(name: String, description: String?, coverColor: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreating = true) }
            collectionRepository.createCollection(name, description, coverColor)
                .onSuccess {
                    _uiState.update { it.copy(isCreating = false, showCreateDialog = false) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isCreating = false, error = e.message) }
                }
        }
    }

    fun loadCollectionDetail(collectionId: String) {
        viewModelScope.launch {
            _detailState.update { it.copy(isLoading = true) }
            try {
                val collection = collectionRepository.getCollectionById(collectionId)
                val quotes = collectionRepository.getQuotesInCollection(collectionId).first()

                _detailState.update {
                    it.copy(
                        isLoading = false,
                        collection = collection,
                        quotes = quotes
                    )
                }
            } catch (e: Exception) {
                _detailState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun refreshCollectionDetail(collectionId: String) {
        viewModelScope.launch {
            _detailState.update { it.copy(isRefreshing = true) }
            try {
                val quotes = collectionRepository.getQuotesInCollection(collectionId).first()
                _detailState.update { it.copy(isRefreshing = false, quotes = quotes) }
            } catch (e: Exception) {
                _detailState.update { it.copy(isRefreshing = false, error = e.message) }
            }
        }
    }

    fun deleteCollection(collectionId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            collectionRepository.deleteCollection(collectionId)
                .onSuccess {
                    _detailState.update { it.copy(showDeleteDialog = false) }
                    onSuccess()
                }
                .onFailure { e ->
                    _detailState.update { it.copy(error = e.message) }
                }
        }
    }

    fun removeQuoteFromCollection(collectionId: String, quoteId: String) {
        viewModelScope.launch {
            collectionRepository.removeQuoteFromCollection(collectionId, quoteId)
                .onSuccess {
                    _detailState.update { state ->
                        state.copy(quotes = state.quotes.filter { it.id != quoteId })
                    }
                }
        }
    }

    fun toggleFavorite(quoteId: String) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(quoteId)
                .onSuccess { isFavorite ->
                    _detailState.update { state ->
                        state.copy(
                            quotes = state.quotes.map {
                                if (it.id == quoteId) it.copy(isFavorite = isFavorite) else it
                            }
                        )
                    }
                }
        }
    }

    fun showDeleteDialog() {
        _detailState.update { it.copy(showDeleteDialog = true) }
    }

    fun hideDeleteDialog() {
        _detailState.update { it.copy(showDeleteDialog = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
        _detailState.update { it.copy(error = null) }
    }
}
