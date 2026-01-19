package com.example.quotevault.presentation.quotedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotevault.domain.model.Collection
import com.example.quotevault.domain.model.Quote
import com.example.quotevault.domain.repository.CollectionRepository
import com.example.quotevault.domain.repository.FavoriteRepository
import com.example.quotevault.domain.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuoteDetailUiState(
    val isLoading: Boolean = false,
    val quote: Quote? = null,
    val collections: List<Collection> = emptyList(),
    val quoteCollectionIds: List<String> = emptyList(),
    val error: String? = null,
    val showShareSheet: Boolean = false,
    val showCollectionSheet: Boolean = false
)

@HiltViewModel
class QuoteDetailViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val favoriteRepository: FavoriteRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuoteDetailUiState())
    val uiState: StateFlow<QuoteDetailUiState> = _uiState.asStateFlow()

    fun loadQuote(quoteId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val quote = quoteRepository.getQuoteById(quoteId)
                val collections = collectionRepository.getCollections().first()
                val quoteCollectionIds = collectionRepository.getCollectionIdsForQuote(quoteId).first()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        quote = quote,
                        collections = collections,
                        quoteCollectionIds = quoteCollectionIds
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun toggleFavorite() {
        val quoteId = _uiState.value.quote?.id ?: return

        viewModelScope.launch {
            favoriteRepository.toggleFavorite(quoteId)
                .onSuccess { isFavorite ->
                    _uiState.update { state ->
                        state.copy(quote = state.quote?.copy(isFavorite = isFavorite))
                    }
                }
        }
    }

    fun showShareSheet() {
        _uiState.update { it.copy(showShareSheet = true) }
    }

    fun hideShareSheet() {
        _uiState.update { it.copy(showShareSheet = false) }
    }

    fun showCollectionSheet() {
        _uiState.update { it.copy(showCollectionSheet = true) }
    }

    fun hideCollectionSheet() {
        _uiState.update { it.copy(showCollectionSheet = false) }
    }

    fun addToCollection(collectionId: String) {
        val quoteId = _uiState.value.quote?.id ?: return

        viewModelScope.launch {
            collectionRepository.addQuoteToCollection(collectionId, quoteId)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            quoteCollectionIds = state.quoteCollectionIds + collectionId,
                            showCollectionSheet = false
                        )
                    }
                }
        }
    }

    fun removeFromCollection(collectionId: String) {
        val quoteId = _uiState.value.quote?.id ?: return

        viewModelScope.launch {
            collectionRepository.removeQuoteFromCollection(collectionId, quoteId)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(quoteCollectionIds = state.quoteCollectionIds - collectionId)
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
