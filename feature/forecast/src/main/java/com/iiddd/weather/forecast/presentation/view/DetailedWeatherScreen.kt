package com.iiddd.weather.forecast.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.core.network.toUiMessage
import com.iiddd.weather.core.ui.components.ErrorScreen
import com.iiddd.weather.core.ui.components.LoadingScreen
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.presentation.viewmodel.ForecastUiState

@Composable
fun DetailedWeatherScreen(
    forecastUiState: ForecastUiState,
    useDeviceLocation: Boolean,
    hasLocationPermission: Boolean,
    isRefreshing: Boolean,
    onRequestLocationPermission: () -> Unit,
    onRefreshRequested: () -> Unit,
    onCurrentLocationRequested: () -> Unit,
) {
    if (useDeviceLocation && !hasLocationPermission) {
        ErrorScreen(
            errorMessage = "Location permission is required to load weather for your current location.",
            onRetry = onRequestLocationPermission,
        )
        return
    }

    when (forecastUiState) {
        is ForecastUiState.Loading -> {
            LoadingScreen()
        }

        is ForecastUiState.Error -> {
            val shouldRetryByRequestingPermission =
                useDeviceLocation && forecastUiState.apiError is ApiError.Input

            ErrorScreen(
                errorMessage = forecastUiState.apiError.toUiMessage(),
                onRetry = if (shouldRetryByRequestingPermission) {
                    onRequestLocationPermission
                } else {
                    onRefreshRequested
                },
            )
        }

        is ForecastUiState.Content -> {
            val weather: Weather = forecastUiState.weather
            val weatherState = rememberUpdatedState(newValue = weather)

            // Show current location button only when viewing weather for explicit coordinates (not device location)
            val showCurrentLocationButton = !useDeviceLocation

            DetailedWeatherScreenContent(
                weatherState = weatherState,
                isRefreshing = isRefreshing,
                showCurrentLocationButton = showCurrentLocationButton,
                onRefresh = onRefreshRequested,
                onCurrentLocationRequested = onCurrentLocationRequested,
            )
        }
    }
}

@WeatherPreview
@Composable
private fun DetailedWeatherScreenPreviewHappyFlow() {
    WeatherTheme {
        val mockState = rememberUpdatedState(
            newValue = Weather(
                currentTemp = 13,
                description = "Clear",
                hourly = emptyList(),
                daily = emptyList(),
            )
        )
        DetailedWeatherScreenContent(
            weatherState = mockState,
            isRefreshing = false,
            showCurrentLocationButton = true,
            onRefresh = {},
            onCurrentLocationRequested = {},
        )
    }
}

@WeatherPreview
@Composable
private fun DetailedWeatherScreenPreviewError() {
    WeatherTheme {
        DetailedWeatherScreen(
            forecastUiState = ForecastUiState.Error(
                apiError = ApiError.Network(
                    message = "Unable to connect to the server.",
                )
            ),
            useDeviceLocation = false,
            hasLocationPermission = false,
            onRequestLocationPermission = {},
            onRefreshRequested = {},
            onCurrentLocationRequested = {},
            isRefreshing = false,
        )
    }
}