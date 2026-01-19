package com.example.quotevault.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    // Auth Flow
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object ResetPassword : Screen("reset_password")

    // Main Flow (Bottom Nav)
    data object Home : Screen("home")
    data object Browse : Screen("browse")
    data object Search : Screen("search")
    data object Favorites : Screen("favorites")
    data object Collections : Screen("collections")

    // Detail Screens
    data object QuoteDetail : Screen("quote/{quoteId}") {
        fun createRoute(quoteId: String) = "quote/$quoteId"
    }
    data object CategoryQuotes : Screen("category/{categoryId}") {
        fun createRoute(categoryId: String) = "category/$categoryId"
    }
    data object CollectionDetail : Screen("collection/{collectionId}") {
        fun createRoute(collectionId: String) = "collection/$collectionId"
    }
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")

    // Share Screen (Bottom Sheet)
    data object ShareQuote : Screen("share/{quoteId}") {
        fun createRoute(quoteId: String) = "share/$quoteId"
    }
}

enum class BottomNavItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val label: String
) {
    Home(
        route = Screen.Home.route,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        label = "Home"
    ),
    Browse(
        route = Screen.Browse.route,
        selectedIcon = Icons.Filled.Category,
        unselectedIcon = Icons.Outlined.Category,
        label = "Browse"
    ),
    Search(
        route = Screen.Search.route,
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        label = "Search"
    ),
    Favorites(
        route = Screen.Favorites.route,
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
        label = "Favorites"
    ),
    Collections(
        route = Screen.Collections.route,
        selectedIcon = Icons.Filled.CollectionsBookmark,
        unselectedIcon = Icons.Outlined.CollectionsBookmark,
        label = "Collections"
    )
}
