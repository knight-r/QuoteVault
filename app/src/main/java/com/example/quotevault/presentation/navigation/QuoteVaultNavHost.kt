package com.example.quotevault.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.quotevault.presentation.auth.login.LoginScreen
import com.example.quotevault.presentation.auth.profile.ProfileScreen
import com.example.quotevault.presentation.auth.resetpassword.ResetPasswordScreen
import com.example.quotevault.presentation.auth.signup.SignUpScreen
import com.example.quotevault.presentation.browse.BrowseScreen
import com.example.quotevault.presentation.browse.CategoryQuotesScreen
import com.example.quotevault.presentation.collections.CollectionDetailScreen
import com.example.quotevault.presentation.collections.CollectionsScreen
import com.example.quotevault.presentation.favorites.FavoritesScreen
import com.example.quotevault.presentation.home.HomeScreen
import com.example.quotevault.presentation.quotedetail.QuoteDetailScreen
import com.example.quotevault.presentation.search.SearchScreen
import com.example.quotevault.presentation.settings.SettingsScreen

@Composable
fun QuoteVaultNavHost(
    navController: NavHostController,
    startDestination: String,
    innerPadding: PaddingValues,
    onShowSnackbar: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding),
        enterTransition = {
            fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        }
    ) {
        // Auth Screens
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                onNavigateToResetPassword = { navController.navigate(Screen.ResetPassword.route) },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onShowSnackbar = onShowSnackbar
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onShowSnackbar = onShowSnackbar
            )
        }

        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(
                onNavigateBack = { navController.popBackStack() },
                onShowSnackbar = onShowSnackbar
            )
        }

        // Main Screens
        composable(Screen.Home.route) {
            HomeScreen(
                onQuoteClick = { quoteId -> navController.navigate(Screen.QuoteDetail.createRoute(quoteId)) },
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onShowSnackbar = onShowSnackbar
            )
        }

        composable(Screen.Browse.route) {
            BrowseScreen(
                onCategoryClick = { categoryId -> navController.navigate(Screen.CategoryQuotes.createRoute(categoryId)) },
                onQuoteClick = { quoteId -> navController.navigate(Screen.QuoteDetail.createRoute(quoteId)) },
                onShowSnackbar = onShowSnackbar
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onQuoteClick = { quoteId -> navController.navigate(Screen.QuoteDetail.createRoute(quoteId)) },
                onShowSnackbar = onShowSnackbar
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onQuoteClick = { quoteId -> navController.navigate(Screen.QuoteDetail.createRoute(quoteId)) },
                onBrowseClick = { navController.navigate(Screen.Browse.route) },
                onShowSnackbar = onShowSnackbar
            )
        }

        composable(Screen.Collections.route) {
            CollectionsScreen(
                onCollectionClick = { collectionId -> navController.navigate(Screen.CollectionDetail.createRoute(collectionId)) },
                onShowSnackbar = onShowSnackbar
            )
        }

        // Detail Screens
        composable(
            route = Screen.QuoteDetail.route,
            arguments = listOf(navArgument("quoteId") { type = NavType.StringType })
        ) { backStackEntry ->
            val quoteId = backStackEntry.arguments?.getString("quoteId") ?: return@composable
            QuoteDetailScreen(
                quoteId = quoteId,
                onNavigateBack = { navController.popBackStack() },
                onShowSnackbar = onShowSnackbar
            )
        }

        composable(
            route = Screen.CategoryQuotes.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: return@composable
            CategoryQuotesScreen(
                categoryId = categoryId,
                onNavigateBack = { navController.popBackStack() },
                onQuoteClick = { quoteId -> navController.navigate(Screen.QuoteDetail.createRoute(quoteId)) },
                onShowSnackbar = onShowSnackbar
            )
        }

        composable(
            route = Screen.CollectionDetail.route,
            arguments = listOf(navArgument("collectionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val collectionId = backStackEntry.arguments?.getString("collectionId") ?: return@composable
            CollectionDetailScreen(
                collectionId = collectionId,
                onNavigateBack = { navController.popBackStack() },
                onQuoteClick = { quoteId -> navController.navigate(Screen.QuoteDetail.createRoute(quoteId)) },
                onShowSnackbar = onShowSnackbar
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onShowSnackbar = onShowSnackbar
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}
