package com.iiddd.weather.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Stable
class WeatherNavigationState(
    initialWeatherDestination: Destination.Weather = Destination.Weather(),
) {
    var currentWeatherDestination: Destination.Weather by mutableStateOf(initialWeatherDestination)
        private set

    fun updateWeatherDestination(destination: Destination.Weather) {
        currentWeatherDestination = destination
    }

    fun resetToDeviceLocation() {
        currentWeatherDestination = Destination.Weather(
            latitude = null,
            longitude = null,
            useDeviceLocation = true,
        )
    }

    companion object {
        val Saver: Saver<WeatherNavigationState, List<Any?>> = Saver(
            save = { state ->
                listOf(
                    state.currentWeatherDestination.latitude,
                    state.currentWeatherDestination.longitude,
                    state.currentWeatherDestination.useDeviceLocation,
                )
            },
            restore = { list ->
                WeatherNavigationState(
                    initialWeatherDestination = Destination.Weather(
                        latitude = list[0] as? Double,
                        longitude = list[1] as? Double,
                        useDeviceLocation = list[2] as? Boolean ?: true,
                    )
                )
            }
        )
    }
}

@Composable
fun rememberWeatherNavigationState(
    initialWeatherDestination: Destination.Weather = Destination.Weather(),
): WeatherNavigationState {
    return rememberSaveable(saver = WeatherNavigationState.Saver) {
        WeatherNavigationState(initialWeatherDestination = initialWeatherDestination)
    }
}
