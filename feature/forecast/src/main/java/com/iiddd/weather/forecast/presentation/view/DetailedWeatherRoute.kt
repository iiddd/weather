package com.iiddd.weather.forecast.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iiddd.weather.forecast.presentation.view.permission.rememberLocationPermissionController
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

    val locationPermissionController = rememberLocationPermissionController()

    val isNavigationCoordinatesProvided = latitude != null && longitude != null
    val shouldRequestDeviceLocation = !isNavigationCoordinatesProvided
    val hasLocationPermission = locationPermissionController.hasLocationPermission

    var hasRequestedLocationPermissionAtStartup by rememberSaveable {
        mutableStateOf(value = false)
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

    LaunchedEffect(
        key1 = latitude,
        key2 = longitude,
        key3 = hasLocationPermission,
    ) {
        val shouldLoadWeather =
            isNavigationCoordinatesProvided ||
                    (shouldRequestDeviceLocation && hasLocationPermission)

        if (!shouldLoadWeather) return@LaunchedEffect

        forecastViewModel.onEvent(
            forecastUiEvent = ForecastUiEvent.LoadWeatherRequested(
                latitude = latitude,
                longitude = longitude,
            ),
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
            forecastViewModel.onEvent(
                forecastUiEvent = ForecastUiEvent.RefreshRequested,
            )
        },
    )
}