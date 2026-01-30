package com.iiddd.weather.forecast.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iiddd.weather.core.ui.components.LoadingScreen
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
    val hasLocationPermission = locationPermissionController.hasLocationPermission

    val isNavigationCoordinatesProvided = latitude != null && longitude != null
    val shouldRequestDeviceLocation = !isNavigationCoordinatesProvided

    var hasRequestedLocationPermissionAtStartup by rememberSaveable { mutableStateOf(false) }
    var permissionStateWhenRequested by rememberSaveable { mutableStateOf<Boolean?>(null) }

    val isAwaitingPermissionResponse = permissionStateWhenRequested != null &&
            permissionStateWhenRequested == hasLocationPermission

    LaunchedEffect(
        shouldRequestDeviceLocation,
        hasLocationPermission,
    ) {
        val shouldRequestPermissionNow =
            shouldRequestDeviceLocation &&
                    !hasLocationPermission &&
                    !hasRequestedLocationPermissionAtStartup

        if (!shouldRequestPermissionNow) return@LaunchedEffect

        hasRequestedLocationPermissionAtStartup = true
        permissionStateWhenRequested = hasLocationPermission
        locationPermissionController.requestLocationPermission()
    }

    // Clear awaiting state once permission status actually changes
    LaunchedEffect(
        key1 = hasLocationPermission,
    ) {
        if (permissionStateWhenRequested != null && permissionStateWhenRequested != hasLocationPermission) {
            permissionStateWhenRequested = null
        }
    }

    LaunchedEffect(
        latitude,
        longitude,
        hasLocationPermission,
        isAwaitingPermissionResponse,
    ) {
        if (isAwaitingPermissionResponse) return@LaunchedEffect

        val shouldLoadWeather =
            isNavigationCoordinatesProvided ||
                    (shouldRequestDeviceLocation && hasLocationPermission)

        if (!shouldLoadWeather) return@LaunchedEffect

        forecastViewModel.onEvent(
            ForecastUiEvent.LoadWeatherRequested(
                latitude = latitude,
                longitude = longitude,
            )
        )
    }

    if (isAwaitingPermissionResponse) {
        LoadingScreen()
        return
    }

    DetailedWeatherScreen(
        forecastUiState = forecastUiState,
        shouldRequestDeviceLocation = shouldRequestDeviceLocation,
        hasLocationPermission = hasLocationPermission,
        onRequestLocationPermission = {
            permissionStateWhenRequested = hasLocationPermission
            locationPermissionController.requestLocationPermission()
        },
        onRefreshRequested = {
            forecastViewModel.onEvent(ForecastUiEvent.RefreshRequested)
        },
    )
}