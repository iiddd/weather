package com.iiddd.weather.favorites.presentation.viewmodel

import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.favorites.presentation.model.FavoriteLocationWithWeather

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState

    data class Content(
        val favorites: List<FavoriteLocationWithWeather>,
        val isRefreshing: Boolean = false,
    ) : FavoritesUiState

    data class Error(
        val apiError: ApiError,
    ) : FavoritesUiState

    data object Empty : FavoritesUiState
}
