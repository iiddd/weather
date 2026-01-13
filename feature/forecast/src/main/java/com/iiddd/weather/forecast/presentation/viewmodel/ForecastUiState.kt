package com.iiddd.weather.forecast.presentation.viewmodel

import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.forecast.domain.model.Weather

sealed interface ForecastUiState {
    data object Loading : ForecastUiState

    data class Content(
        val detailedWeatherContent: DetailedWeatherContent
    ) : ForecastUiState

    data class Error(
        val apiError: ApiError
    ) : ForecastUiState
}

data class DetailedWeatherContent(
    val weather: Weather
)