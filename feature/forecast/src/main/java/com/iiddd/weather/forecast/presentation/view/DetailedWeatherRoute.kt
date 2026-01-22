package com.iiddd.weather.forecast.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iiddd.weather.forecast.presentation.logic.WeatherLoadInput
import com.iiddd.weather.forecast.presentation.view.permission.rememberLocationPermissionController
import com.iiddd.weather.forecast.presentation.viewmodel.ForecastUiEvent
import com.iiddd.weather.forecast.presentation.viewmodel.ForecastUiState
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

    val locationPermissionController = rememberLocationPermissionController()

    val shouldRequestDeviceLocation = latitude == null || longitude == null
    val hasLocationPermission = locationPermissionController.hasLocationPermission
    val isContentVisible = forecastUiState is ForecastUiState.Content

    val loadInput = WeatherLoadInput(
        latitude = latitude,
        longitude = longitude,
        hasLocationPermission = hasLocationPermission,
        shouldRequestDeviceLocation = shouldRequestDeviceLocation,
        isContentVisible = isContentVisible,
    )

    var hasRequestedLocationPermissionAtStartup by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(
        key1 = shouldRequestDeviceLocation,
        key2 = hasLocationPermission,
    ) {
        val shouldRequestPermissionNow =
            shouldRequestDeviceLocation &&
                    !hasLocationPermission &&
                    !hasRequestedLocationPermissionAtStartup

        if (!shouldRequestPermissionNow) return@LaunchedEffect

        hasRequestedLocationPermissionAtStartup = true
        locationPermissionController.requestLocationPermission()
    }

    LaunchedEffect(loadInput) {
        if (!loadInput.shouldLoadWeather) return@LaunchedEffect

        forecastViewModel.onEvent(
            ForecastUiEvent.LoadWeatherRequested(
                latitude = loadInput.latitude,
                longitude = loadInput.longitude,
            )
        )
    }

    DetailedWeatherScreen(
        forecastUiState = forecastUiState,
        shouldRequestDeviceLocation = shouldRequestDeviceLocation,
        hasLocationPermission = hasLocationPermission,
        onRequestLocationPermission = {
            locationPermissionController.requestLocationPermission()
        },
        onRefreshRequested = {
            forecastViewModel.onEvent(ForecastUiEvent.RefreshRequested)
        },
    )
}