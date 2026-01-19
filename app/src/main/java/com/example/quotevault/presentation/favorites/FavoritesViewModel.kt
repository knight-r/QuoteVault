package com.example.quotevault.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quotevault.domain.model.Quote
import com.example.quotevault.domain.repository.AuthRepository
import com.example.quotevault.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val favorites: List<Quote> = emptyList(),
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
        observeFavorites()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.isLoggedIn.collect { isLoggedIn ->
                _uiState.update { it.copy(isLoggedIn = isLoggedIn) }
                if (isLoggedIn) {
                    syncFavorites()
                }
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            favoriteRepository.getFavoriteQuotes().collect { favorites ->
                _uiState.update { it.copy(isLoading = false, favorites = favorites) }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            try {
                favoriteRepository.syncFavorites()
                _uiState.update { it.copy(isRefreshing = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isRefreshing = false, error = e.message) }
            }
        }
    }

    private fun syncFavorites() {
        viewModelScope.launch {
            try {
                favoriteRepository.syncFavorites()
            } catch (e: Exception) {
                // Silently fail, local data is available
            }
        }
    }

    fun removeFavorite(quoteId: String) {
        viewModelScope.launch {
            favoriteRepository.removeFavorite(quoteId)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
