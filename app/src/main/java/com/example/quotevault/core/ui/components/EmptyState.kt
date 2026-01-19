package com.example.quotevault.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class EmptyStateType {
    QUOTES,
    FAVORITES,
    COLLECTIONS,
    SEARCH
}

@Composable
fun EmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    type: EmptyStateType = EmptyStateType.QUOTES,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    val icon: ImageVector = when (type) {
        EmptyStateType.QUOTES -> Icons.Outlined.FormatQuote
        EmptyStateType.FAVORITES -> Icons.Outlined.FavoriteBorder
        EmptyStateType.COLLECTIONS -> Icons.Outlined.CollectionsBookmark
        EmptyStateType.SEARCH -> Icons.Outlined.SearchOff
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if (actionLabel != null && onAction != null) {
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onAction) {
                Text(text = actionLabel)
            }
        }
    }
}

@Composable
fun EmptyFavoritesState(
    onBrowseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyState(
        title = "No Favorites Yet",
        description = "Start adding quotes to your favorites by tapping the heart icon on quotes you love.",
        type = EmptyStateType.FAVORITES,
        actionLabel = "Browse Quotes",
        onAction = onBrowseClick,
        modifier = modifier
    )
}

@Composable
fun EmptyCollectionsState(
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyState(
        title = "No Collections Yet",
        description = "Create collections to organize your favorite quotes by theme, mood, or purpose.",
        type = EmptyStateType.COLLECTIONS,
        actionLabel = "Create Collection",
        onAction = onCreateClick,
        modifier = modifier
    )
}

@Composable
fun EmptySearchState(
    query: String,
    modifier: Modifier = Modifier
) {
    EmptyState(
        title = "No Results Found",
        description = "We couldn't find any quotes matching \"$query\". Try different keywords.",
        type = EmptyStateType.SEARCH,
        modifier = modifier
    )
}

@Composable
fun EmptyCollectionQuotesState(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyState(
        title = "Collection is Empty",
        description = "Add quotes to this collection to see them here.",
        type = EmptyStateType.QUOTES,
        actionLabel = "Add Quotes",
        onAction = onAddClick,
        modifier = modifier
    )
}
