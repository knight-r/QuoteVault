package com.example.quotevault.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotevault.domain.model.Quote
import com.example.quotevault.domain.repository.FavoriteRepository
import com.example.quotevault.domain.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val results: List<Quote> = emptyList(),
    val error: String? = null,
    val hasSearched: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }

        // Debounce search
        searchJob?.cancel()
        if (query.isBlank()) {
            _uiState.update { it.copy(results = emptyList(), hasSearched = false) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(300) // Debounce
            search(query)
        }
    }

    fun search(query: String = _uiState.value.query) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true, error = null) }

            try {
                val results = quoteRepository.searchQuotes(query).first()
                _uiState.update {
                    it.copy(
                        isSearching = false,
                        results = results,
                        hasSearched = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSearching = false,
                        error = e.message,
                        hasSearched = true
                    )
                }
            }
        }
    }

    fun toggleFavorite(quoteId: String) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(quoteId)
                .onSuccess { isFavorite ->
                    _uiState.update { state ->
                        state.copy(
                            results = state.results.map {
                                if (it.id == quoteId) it.copy(isFavorite = isFavorite) else it
                            }
                        )
                    }
                }
        }
    }

    fun clearSearch() {
        _uiState.update { SearchUiState() }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
