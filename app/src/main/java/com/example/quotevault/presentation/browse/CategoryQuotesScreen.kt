package com.example.quotevault.presentation.browse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quotevault.core.ui.components.EmptyState
import com.example.quotevault.core.ui.components.EmptyStateType
import com.example.quotevault.core.ui.components.InlineLoadingIndicator
import com.example.quotevault.core.ui.components.LoadingIndicator
import com.example.quotevault.core.ui.components.QuoteCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryQuotesScreen(
    categoryId: String,
    onNavigateBack: () -> Unit,
    onQuoteClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    viewModel: BrowseViewModel = hiltViewModel()
) {
    val uiState by viewModel.categoryQuotesState.collectAsState()
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleIndex >= uiState.quotes.size - 3 && uiState.hasMorePages && !uiState.isLoadingMore
        }
    }

    LaunchedEffect(categoryId) {
        viewModel.loadCategoryQuotes(categoryId)
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadMoreCategoryQuotes(categoryId)
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            onShowSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.category?.displayName ?: "Category",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.refreshCategoryQuotes(categoryId) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingIndicator(message = "Loading quotes...")
                }
                uiState.quotes.isEmpty() -> {
                    EmptyState(
                        title = "No Quotes Found",
                        description = "There are no quotes in this category yet.",
                        type = EmptyStateType.QUOTES
                    )
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = uiState.quotes,
                            key = { it.id }
                        ) { quote ->
                            QuoteCard(
                                quote = quote,
                                onQuoteClick = { onQuoteClick(quote.id) },
                                onFavoriteClick = { viewModel.toggleFavorite(quote.id) },
                                onShareClick = { /* TODO */ },
                                onAddToCollectionClick = { /* TODO */ },
                                showCategory = false
                            )
                        }

                        if (uiState.isLoadingMore) {
                            item(key = "loading") {
                                InlineLoadingIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}
