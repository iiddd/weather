package com.iiddd.weather.forecast.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.presentation.view.location.rememberCurrentLocationCoordinatesProvider

@Composable
fun DetailedWeatherScreen(
    weatherState: State<Weather?>,
    isLoading: Boolean,
    initialLatitude: Double?,
    initialLongitude: Double?,
    initialCity: String?,
    loadWeather: (latitude: Double, longitude: Double, cityOverride: String?) -> Unit
) {
    if (initialLatitude != null && initialLongitude != null) {
        DetailedWeatherInitialCoordinatesSection(
            weatherState = weatherState,
            isLoading = isLoading,
            initialLatitude = initialLatitude,
            initialLongitude = initialLongitude,
            initialCity = initialCity,
            loadWeatherForInitialCoordinates = { latitude: Double, longitude: Double, city: String? ->
                loadWeather(
                    latitude,
                    longitude,
                    city
                )
            }
        )
        return
    }

    val locationCoordinatesProvider = rememberCurrentLocationCoordinatesProvider()

    DetailedWeatherDeviceLocationSection(
        weatherState = weatherState,
        isLoading = isLoading,
        loadWeatherForCoordinates = { latitude: Double, longitude: Double ->
            loadWeather(
                latitude,
                longitude,
                null
            )
        },
        locationCoordinatesProvider = locationCoordinatesProvider
    )
}