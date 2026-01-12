package com.iiddd.weather.forecast.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import com.iiddd.weather.core.ui.components.LoadingScreen
import com.iiddd.weather.forecast.domain.model.Weather

@Composable
internal fun DetailedWeatherInitialCoordinatesSection(
    weatherState: State<Weather?>,
    isLoading: Boolean,
    initialLatitude: Double,
    initialLongitude: Double,
    initialCity: String?,
    loadWeatherForInitialCoordinates: (latitude: Double, longitude: Double, city: String?) -> Unit
) {
    LaunchedEffect(
        key1 = initialLatitude,
        key2 = initialLongitude,
        key3 = initialCity
    ) {
        loadWeatherForInitialCoordinates(
            initialLatitude,
            initialLongitude,
            initialCity
        )
    }

    if (isLoading) {
        LoadingScreen()
        return
    }

    DetailedWeatherContent(
        weatherState = weatherState,
        onRefresh = {
            loadWeatherForInitialCoordinates(
                initialLatitude,
                initialLongitude,
                initialCity
            )
        }
    )
}