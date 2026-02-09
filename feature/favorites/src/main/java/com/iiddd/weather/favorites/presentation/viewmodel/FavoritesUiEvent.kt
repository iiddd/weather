package com.iiddd.weather.favorites.presentation.viewmodel

import com.iiddd.weather.favorites.presentation.model.FavoriteLocationWithWeather

sealed interface FavoritesUiEvent {
    data object LoadFavoritesRequested : FavoritesUiEvent
    data object RefreshRequested : FavoritesUiEvent
    data class DeleteFavoriteRequested(
        val favoriteLocationWithWeather: FavoriteLocationWithWeather,
    ) : FavoritesUiEvent
}
