package com.iiddd.weather.favorites.presentation.view

import androidx.compose.runtime.Composable
import com.iiddd.weather.core.network.toUiMessage
import com.iiddd.weather.core.ui.components.ErrorScreen
import com.iiddd.weather.core.ui.components.LoadingScreen
import com.iiddd.weather.favorites.presentation.model.FavoriteLocationWithWeather
import com.iiddd.weather.favorites.presentation.viewmodel.FavoritesUiState

@Composable
fun FavoritesScreen(
    favoritesUiState: FavoritesUiState,
    isRefreshing: Boolean,
    onRefreshRequested: () -> Unit,
    onOpenDetails: (latitude: Double, longitude: Double) -> Unit,
    onDeleteFavorite: (FavoriteLocationWithWeather) -> Unit,
) {
    when (favoritesUiState) {
        is FavoritesUiState.Loading -> {
            LoadingScreen()
        }

        is FavoritesUiState.Error -> {
            ErrorScreen(
                errorMessage = favoritesUiState.apiError.toUiMessage(),
                onRetry = onRefreshRequested,
            )
        }

        is FavoritesUiState.Empty -> {
            FavoritesEmptyScreenContent()
        }

        is FavoritesUiState.Content -> {
            FavoritesScreenContent(
                favorites = favoritesUiState.favorites,
                isRefreshing = isRefreshing,
                onRefresh = onRefreshRequested,
                onFavoriteClicked = { favoriteLocationWithWeather ->
                    onOpenDetails(
                        favoriteLocationWithWeather.favoriteLocation.latitude,
                        favoriteLocationWithWeather.favoriteLocation.longitude,
                    )
                },
                onDeleteFavorite = onDeleteFavorite,
            )
        }
    }
}
