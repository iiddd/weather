package com.iiddd.weather.forecast.presentation.viewmodel

import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.forecast.domain.model.Weather

sealed interface ForecastUiState {
    data object Loading : ForecastUiState

    data class Content(
        val weather: Weather,
        val isRefreshing: Boolean = false,
        val isFavorite: Boolean = false,
    ) : ForecastUiState

    data class Error(
        val apiError: ApiError
    ) : ForecastUiState
}
