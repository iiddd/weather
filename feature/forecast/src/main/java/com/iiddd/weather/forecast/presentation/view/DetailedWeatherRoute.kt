package com.iiddd.weather.forecast.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iiddd.weather.forecast.presentation.viewmodel.ForecastUiEvent
import com.iiddd.weather.forecast.presentation.viewmodel.ForecastViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailedWeatherRoute(
    forecastViewModel: ForecastViewModel = koinViewModel(),
    latitude: Double?,
    longitude: Double?,
) {
    val forecastUiState by forecastViewModel
        .forecastUiState
        .collectAsStateWithLifecycle()

    LaunchedEffect(
        key1 = latitude,
        key2 = longitude,
    ) {
        forecastViewModel.onEvent(
            forecastUiEvent = ForecastUiEvent.LoadWeatherRequested(
                latitude = latitude,
                longitude = longitude,
            )
        )
    }

    DetailedWeatherScreen(
        forecastUiState = forecastUiState,
        onRefreshRequested = {
            forecastViewModel.onEvent(
                forecastUiEvent = ForecastUiEvent.RefreshRequested
            )
        }
    )
}