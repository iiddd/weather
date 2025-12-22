package com.iiddd.weather.forecast.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.iiddd.weather.forecast.presentation.viewmodel.ForecastViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailedWeatherRoute(
    viewModel: ForecastViewModel = koinViewModel(),
    initialLatitude: Double? = null,
    initialLongitude: Double? = null,
    initialCity: String? = null
) {
    val weatherState = viewModel.weather.collectAsState()
    val isLoadingState = viewModel.isLoading.collectAsState()

    DetailedWeatherScreen(
        weatherState = weatherState,
        isLoading = isLoadingState.value,
        initialLatitude = initialLatitude,
        initialLongitude = initialLongitude,
        initialCity = initialCity,
        loadWeather = { latitude: Double, longitude: Double, cityOverride: String? ->
            viewModel.loadWeather(
                latitude = latitude,
                longitude = longitude,
                cityOverride = cityOverride
            )
        }
    )
}