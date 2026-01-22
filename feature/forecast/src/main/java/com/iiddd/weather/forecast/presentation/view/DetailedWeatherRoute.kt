package com.iiddd.weather.forecast.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iiddd.weather.forecast.presentation.view.permission.rememberLocationPermissionController
import com.iiddd.weather.forecast.presentation.viewmodel.ForecastUiEvent
import com.iiddd.weather.forecast.presentation.viewmodel.ForecastViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

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

    val destinationKey = remember(latitude, longitude) {
        val lat = latitude?.toStableString() ?: DEVICE_LOCATION_KEY
        val lon = longitude?.toStableString() ?: DEVICE_LOCATION_KEY
        "$DETAILED_WEATHER_ROUTE_PREFIX:$lat,$lon"
    }

    val loadKey = remember(
        destinationKey,
        hasLocationPermission,
        shouldRequestDeviceLocation,
        isNavigationCoordinatesProvided,
    ) {
        LoadKey(
            destinationKey = destinationKey,
            hasLocationPermission = hasLocationPermission,
            shouldRequestDeviceLocation = shouldRequestDeviceLocation,
            isNavigationCoordinatesProvided = isNavigationCoordinatesProvided,
        )
    }

    var latestLoadedDestinationKey by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = loadKey) {
        val shouldLoadWeather =
            loadKey.isNavigationCoordinatesProvided ||
                    (loadKey.shouldRequestDeviceLocation && loadKey.hasLocationPermission)

        if (!shouldLoadWeather) return@LaunchedEffect
        if (latestLoadedDestinationKey == loadKey.destinationKey) return@LaunchedEffect

        latestLoadedDestinationKey = loadKey.destinationKey

        forecastViewModel.onEvent(
            ForecastUiEvent.LoadWeatherRequested(
                latitude = latitude,
                longitude = longitude,
            )
        )
    }

    DetailedWeatherScreen(
        forecastUiState = forecastUiState,
        shouldRequestDeviceLocation = shouldRequestDeviceLocation,
        hasLocationPermission = hasLocationPermission,
        onRequestLocationPermission = { locationPermissionController.requestLocationPermission() },
        onRefreshRequested = {
            latestLoadedDestinationKey = null
            forecastViewModel.onEvent(ForecastUiEvent.RefreshRequested)
        },
    )
}

private data class LoadKey(
    val destinationKey: String,
    val hasLocationPermission: Boolean,
    val shouldRequestDeviceLocation: Boolean,
    val isNavigationCoordinatesProvided: Boolean,
)

private const val DEVICE_LOCATION_KEY = "device"
private const val DETAILED_WEATHER_ROUTE_PREFIX = "DetailedWeather"
private const val COORDINATE_KEY_DECIMALS = 5

private fun Double.toStableString(): String =
    String.format(
        Locale.US,
        "%.${COORDINATE_KEY_DECIMALS}f",
        this,
    )