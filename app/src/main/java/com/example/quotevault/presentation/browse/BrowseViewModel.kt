package com.example.quotevault.presentation.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotevault.domain.model.Category
import com.example.quotevault.domain.model.Quote
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

data class BrowseUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: String? = null
)

data class CategoryQuotesUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val category: Category? = null,
    val quotes: List<Quote> = emptyList(),
    val error: String? = null,
    val currentPage: Int = 0,
    val hasMorePages: Boolean = true,
    val isLoadingMore: Boolean = false
)

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrowseUiState())
    val uiState: StateFlow<BrowseUiState> = _uiState.asStateFlow()

    private val _categoryQuotesState = MutableStateFlow(CategoryQuotesUiState())
    val categoryQuotesState: StateFlow<CategoryQuotesUiState> = _categoryQuotesState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                quoteRepository.refreshCategories()
                quoteRepository.getCategories().collect { categories ->
                    _uiState.update { it.copy(isLoading = false, categories = categories) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun loadCategoryQuotes(categoryId: String) {
        viewModelScope.launch {
            _categoryQuotesState.update { it.copy(isLoading = true, quotes = emptyList()) }
            try {
                val category = quoteRepository.getCategoryById(categoryId)
                val quotes = quoteRepository.getQuotesByCategory(categoryId).first()

                _categoryQuotesState.update {
                    it.copy(
                        isLoading = false,
                        category = category,
                        quotes = quotes,
                        hasMorePages = quotes.size >= 20
                    )
                }
            } catch (e: Exception) {
                _categoryQuotesState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun refreshCategoryQuotes(categoryId: String) {
        viewModelScope.launch {
            _categoryQuotesState.update { it.copy(isRefreshing = true) }
            try {
                quoteRepository.refreshQuotes()
                val quotes = quoteRepository.getQuotesByCategory(categoryId).first()

                _categoryQuotesState.update {
                    it.copy(
                        isRefreshing = false,
                        quotes = quotes,
                        currentPage = 0,
                        hasMorePages = quotes.size >= 20
                    )
                }
            } catch (e: Exception) {
                _categoryQuotesState.update { it.copy(isRefreshing = false, error = e.message) }
            }
        }
    }

    fun loadMoreCategoryQuotes(categoryId: String) {
        if (_categoryQuotesState.value.isLoadingMore || !_categoryQuotesState.value.hasMorePages) return

        viewModelScope.launch {
            _categoryQuotesState.update { it.copy(isLoadingMore = true) }
            try {
                val nextPage = _categoryQuotesState.value.currentPage + 1
                val newQuotes = quoteRepository.getQuotesByCategoryPaginated(categoryId, nextPage, 20)

                _categoryQuotesState.update {
                    it.copy(
                        isLoadingMore = false,
                        quotes = it.quotes + newQuotes,
                        currentPage = nextPage,
                        hasMorePages = newQuotes.size == 20
                    )
                }
            } catch (e: Exception) {
                _categoryQuotesState.update { it.copy(isLoadingMore = false) }
            }
        }
    }

    fun toggleFavorite(quoteId: String) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(quoteId)
                .onSuccess { isFavorite ->
                    _categoryQuotesState.update { state ->
                        state.copy(
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
        _categoryQuotesState.update { it.copy(error = null) }
    }
}
