package com.example.quotevault.presentation.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quotevault.core.ui.components.EmptyFavoritesState
import com.example.quotevault.core.ui.components.LoadingIndicator
import com.example.quotevault.core.ui.components.QuoteCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onQuoteClick: (String) -> Unit,
    onBrowseClick: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            onShowSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingIndicator(message = "Loading favorites...")
                }
                !uiState.isLoggedIn -> {
                    EmptyFavoritesState(
                        onBrowseClick = onBrowseClick
                    )
                }
                uiState.favorites.isEmpty() -> {
                    EmptyFavoritesState(
                        onBrowseClick = onBrowseClick
                    )
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = uiState.favorites,
                            key = { it.id }
                        ) { quote ->
                            QuoteCard(
                                quote = quote,
                                onQuoteClick = { onQuoteClick(quote.id) },
                                onFavoriteClick = { viewModel.removeFavorite(quote.id) },
                                onShareClick = { /* TODO */ },
                                onAddToCollectionClick = { /* TODO */ }
                            )
                        }
                    }
                }
            }
        }
    }
}
