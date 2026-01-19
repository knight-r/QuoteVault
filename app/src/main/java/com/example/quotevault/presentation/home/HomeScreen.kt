package com.example.quotevault.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quotevault.core.ui.components.EmptyState
import com.example.quotevault.core.ui.components.EmptyStateType
import com.example.quotevault.core.ui.components.InlineLoadingIndicator
import com.example.quotevault.core.ui.components.LoadingIndicator
import com.example.quotevault.core.ui.components.QuoteCard
import com.example.quotevault.core.ui.theme.CustomShapes
import com.example.quotevault.core.ui.theme.GradientOcean
import com.example.quotevault.core.ui.theme.LocalFontSizePreference
import com.example.quotevault.core.ui.theme.QuoteTextStyles
import com.example.quotevault.domain.model.Quote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onQuoteClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleIndex >= uiState.quotes.size - 3 && uiState.hasMorePages && !uiState.isLoadingMore
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadMore()
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
                        "QuoteVault",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    if (uiState.isLoggedIn) {
                        IconButton(onClick = onProfileClick) {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                        }
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
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
                    LoadingIndicator(message = "Loading quotes...")
                }
                uiState.quotes.isEmpty() && !uiState.isLoading -> {
                    EmptyState(
                        title = "No Quotes Yet",
                        description = "Pull down to refresh and load quotes.",
                        type = EmptyStateType.QUOTES,
                        actionLabel = "Refresh",
                        onAction = { viewModel.refresh() }
                    )
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Quote of the Day
                        uiState.quoteOfDay?.let { qod ->
                            item(key = "qod") {
                                QuoteOfDayCard(
                                    quote = qod,
                                    onClick = { onQuoteClick(qod.id) }
                                )
                            }
                        }

                        // Section Header
                        item(key = "header") {
                            Text(
                                text = "Recent Quotes",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                        }

                        // Quote List
                        items(
                            items = uiState.quotes,
                            key = { it.id }
                        ) { quote ->
                            QuoteCard(
                                quote = quote,
                                onQuoteClick = { onQuoteClick(quote.id) },
                                onFavoriteClick = { viewModel.toggleFavorite(quote.id) },
                                onShareClick = { /* TODO: Share */ },
                                onAddToCollectionClick = { /* TODO: Add to collection */ }
                            )
                        }

                        // Loading More Indicator
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

@Composable
fun QuoteOfDayCard(
    quote: Quote,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fontSizePreference = LocalFontSizePreference.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = CustomShapes.QuoteCard,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(colors = GradientOcean)
                )
                .padding(24.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Quote of the Day",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "\"${quote.text}\"",
                    style = QuoteTextStyles.quoteTextLarge(fontSizePreference),
                    color = MaterialTheme.colorScheme.surface,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "— ${quote.author}",
                    style = QuoteTextStyles.authorText(fontSizePreference),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
