package com.iiddd.weather.favorites.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iiddd.weather.favorites.presentation.model.FavoriteLocationWithWeather
import com.iiddd.weather.favorites.presentation.viewmodel.FavoritesUiEvent
import com.iiddd.weather.favorites.presentation.viewmodel.FavoritesUiState
import com.iiddd.weather.favorites.presentation.viewmodel.FavoritesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesRoute(
    favoritesViewModel: FavoritesViewModel = koinViewModel(),
    onOpenDetails: (latitude: Double, longitude: Double) -> Unit,
) {
    val favoritesUiState by favoritesViewModel
        .favoritesUiState
        .collectAsStateWithLifecycle()

    val isRefreshing = (favoritesUiState as? FavoritesUiState.Content)?.isRefreshing ?: false

    LaunchedEffect(Unit) {
        favoritesViewModel.onEvent(FavoritesUiEvent.LoadFavoritesRequested)
    }

    FavoritesScreen(
        favoritesUiState = favoritesUiState,
        isRefreshing = isRefreshing,
        onRefreshRequested = {
            favoritesViewModel.onEvent(FavoritesUiEvent.RefreshRequested)
        },
        onOpenDetails = onOpenDetails,
        onDeleteFavorite = { favoriteLocationWithWeather: FavoriteLocationWithWeather ->
            favoritesViewModel.onEvent(
                FavoritesUiEvent.DeleteFavoriteRequested(
                    favoriteLocationWithWeather = favoriteLocationWithWeather,
                )
            )
        },
    )
}
