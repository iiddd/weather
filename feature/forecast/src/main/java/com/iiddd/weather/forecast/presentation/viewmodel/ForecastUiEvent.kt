package com.iiddd.weather.forecast.presentation.viewmodel

sealed interface ForecastUiEvent {
    data class LoadWeatherRequested(
        val latitude: Double?,
        val longitude: Double?,
    ) : ForecastUiEvent

    data object RefreshRequested : ForecastUiEvent

    data object ErrorDismissed : ForecastUiEvent
}