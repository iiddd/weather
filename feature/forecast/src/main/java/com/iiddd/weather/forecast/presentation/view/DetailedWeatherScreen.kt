package com.iiddd.weather.forecast.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.iiddd.weather.core.network.toUiMessage
import com.iiddd.weather.core.ui.components.ErrorScreen
import com.iiddd.weather.core.ui.components.LoadingScreen
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.presentation.viewmodel.ForecastUiState

@Composable
fun DetailedWeatherScreen(
    forecastUiState: ForecastUiState,
    onRefreshRequested: () -> Unit,
) {
    when (forecastUiState) {
        is ForecastUiState.Loading -> {
            LoadingScreen()
        }

        is ForecastUiState.Error -> {
            ErrorScreen(
                errorMessage = forecastUiState.apiError.toUiMessage(),
                onRetry = onRefreshRequested,
            )
        }

        is ForecastUiState.Content -> {
            val weather: Weather = forecastUiState
                .detailedWeatherContent
                .weather

            val weatherState: State<Weather?> = remember(key1 = weather) {
                mutableStateOf(value = weather)
            }

            DetailedWeatherScreenContent(
                weatherState = weatherState,
                onRefresh = onRefreshRequested,
            )
        }
    }
}