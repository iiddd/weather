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
import com.iiddd.weather.forecast.presentation.viewmodel.ForecastUiState
import com.iiddd.weather.forecast.presentation.viewmodel.ForecastViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailedWeatherRoute(
    forecastViewModel: ForecastViewModel = koinViewModel(),
    latitude: Double?,
    longitude: Double?,
    useDeviceLocation: Boolean,
    onNavigateToDeviceLocation: () -> Unit,
) {
    val forecastUiState by forecastViewModel
        .forecastUiState
        .collectAsStateWithLifecycle()

    val locationPermissionController = rememberLocationPermissionController()
    val hasLocationPermission = locationPermissionController.hasLocationPermission

    val hasExplicitCoordinates = latitude != null && longitude != null

    var hasRequestedLocationPermissionAtStartup by rememberSaveable { mutableStateOf(false) }
    var permissionStateWhenRequested by rememberSaveable { mutableStateOf<Boolean?>(null) }

    val isAwaitingPermissionResponse = permissionStateWhenRequested != null &&
            permissionStateWhenRequested == hasLocationPermission

    val isRefreshing = (forecastUiState as? ForecastUiState.Content)?.isRefreshing ?: false

    // Request permission at startup if using device location and don't have permission
    LaunchedEffect(
        useDeviceLocation,
        hasLocationPermission,
    ) {
        val shouldRequestPermissionNow =
            useDeviceLocation &&
                    !hasLocationPermission &&
                    !hasRequestedLocationPermissionAtStartup

        if (!shouldRequestPermissionNow) return@LaunchedEffect

        hasRequestedLocationPermissionAtStartup = true
        permissionStateWhenRequested = hasLocationPermission
        locationPermissionController.requestLocationPermission()
    }

    // Clear awaiting state when permission changes
    LaunchedEffect(key1 = hasLocationPermission) {
        if (permissionStateWhenRequested != null && permissionStateWhenRequested != hasLocationPermission) {
            permissionStateWhenRequested = null
        }
    }

    // Load weather when conditions are met
    LaunchedEffect(
        latitude,
        longitude,
        useDeviceLocation,
        hasLocationPermission,
        isAwaitingPermissionResponse,
    ) {
        if (isAwaitingPermissionResponse) return@LaunchedEffect

        val shouldLoadWeather = when {
            hasExplicitCoordinates -> true
            useDeviceLocation && hasLocationPermission -> true
            else -> false
        }

        if (!shouldLoadWeather) return@LaunchedEffect

        forecastViewModel.onEvent(
            ForecastUiEvent.LoadWeatherRequested(
                latitude = latitude,
                longitude = longitude,
                useDeviceLocation = useDeviceLocation,
            )
        )
    }

    if (isAwaitingPermissionResponse) {
        LoadingScreen()
        return
    }

    DetailedWeatherScreen(
        forecastUiState = forecastUiState,
        useDeviceLocation = useDeviceLocation,
        hasLocationPermission = hasLocationPermission,
        isRefreshing = isRefreshing,
        onRequestLocationPermission = {
            permissionStateWhenRequested = hasLocationPermission
            locationPermissionController.requestLocationPermission()
        },
        onRefreshRequested = {
            forecastViewModel.onEvent(ForecastUiEvent.RefreshRequested)
        },
        onCurrentLocationRequested = onNavigateToDeviceLocation,
    )
}
