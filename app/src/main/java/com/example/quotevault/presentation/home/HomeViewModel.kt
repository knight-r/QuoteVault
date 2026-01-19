package com.example.quotevault.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotevault.domain.model.Quote
import com.example.quotevault.domain.repository.AuthRepository
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

data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val quoteOfDay: Quote? = null,
    val quotes: List<Quote> = emptyList(),
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val currentPage: Int = 0,
    val hasMorePages: Boolean = true,
    val isLoadingMore: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val favoriteRepository: FavoriteRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
        loadData()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.isLoggedIn.collect { isLoggedIn ->
                _uiState.update { it.copy(isLoggedIn = isLoggedIn) }
            }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Refresh from remote
                quoteRepository.refreshQuotes()
                quoteRepository.refreshCategories()

                // Get quote of day
                val qod = quoteRepository.getQuoteOfDay()

                // Get first page of quotes
                val quotes = quoteRepository.getQuotes().first()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        quoteOfDay = qod,
                        quotes = quotes.take(20),
                        currentPage = 0,
                        hasMorePages = quotes.size > 20
                    )
                }
            } catch (e: Exception) {
                // Try to load from cache
                try {
                    val quotes = quoteRepository.getQuotes().first()
                    val qod = quotes.firstOrNull()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            quoteOfDay = qod,
                            quotes = quotes,
                            error = if (quotes.isEmpty()) "No quotes available" else null
                        )
                    }
                } catch (e2: Exception) {
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message ?: "Failed to load quotes")
                    }
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }

            try {
                quoteRepository.refreshQuotes()
                val qod = quoteRepository.getQuoteOfDay()
                val quotes = quoteRepository.getQuotes().first()

                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        quoteOfDay = qod,
                        quotes = quotes.take(20),
                        currentPage = 0,
                        hasMorePages = quotes.size > 20
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isRefreshing = false, error = e.message)
                }
            }
        }
    }

    fun loadMore() {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMorePages) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }

            try {
                val nextPage = _uiState.value.currentPage + 1
                val newQuotes = quoteRepository.getQuotesPaginated(nextPage, 20)

                _uiState.update {
                    it.copy(
                        isLoadingMore = false,
                        quotes = it.quotes + newQuotes,
                        currentPage = nextPage,
                        hasMorePages = newQuotes.size == 20
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingMore = false) }
            }
        }
    }

    fun toggleFavorite(quoteId: String) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(quoteId)
                .onSuccess { isFavorite ->
                    // Update local state
                    _uiState.update { state ->
                        state.copy(
                            quoteOfDay = state.quoteOfDay?.let {
                                if (it.id == quoteId) it.copy(isFavorite = isFavorite) else it
                            },
                            quotes = state.quotes.map {
                                if (it.id == quoteId) it.copy(isFavorite = isFavorite) else it
                            }
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
