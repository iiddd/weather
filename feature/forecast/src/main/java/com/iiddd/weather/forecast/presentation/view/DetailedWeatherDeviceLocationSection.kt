package com.iiddd.weather.forecast.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.iiddd.weather.core.ui.components.LoadingScreen
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.presentation.view.location.CurrentLocationCoordinatesProvider
import com.iiddd.weather.forecast.presentation.view.permission.rememberLocationPermissionController
import kotlinx.coroutines.launch

@Composable
internal fun DetailedWeatherDeviceLocationSection(
    weatherState: State<Weather?>,
    isLoading: Boolean,
    loadWeatherForCoordinates: (latitude: Double, longitude: Double) -> Unit,
    locationCoordinatesProvider: CurrentLocationCoordinatesProvider
) {
    val coroutineScope = rememberCoroutineScope()

    var hasRequestedWeatherForCurrentLocation by rememberSaveable { mutableStateOf(false) }
    var isLocationFlowLoading by rememberSaveable { mutableStateOf(true) }

    val locationPermissionController = rememberLocationPermissionController()

    LaunchedEffect(key1 = Unit) {
        if (!locationPermissionController.hasLocationPermission) {
            isLocationFlowLoading = true
            locationPermissionController.requestLocationPermission()
        }
    }

    LaunchedEffect(key1 = locationPermissionController.hasLocationPermission) {
        if (!locationPermissionController.hasLocationPermission) {
            isLocationFlowLoading = true
            hasRequestedWeatherForCurrentLocation = false
            return@LaunchedEffect
        }

        if (hasRequestedWeatherForCurrentLocation) return@LaunchedEffect

        isLocationFlowLoading = true

        val coordinates = locationCoordinatesProvider.getCurrentCoordinatesOrNull()
        if (coordinates == null) {
            isLocationFlowLoading = true
            return@LaunchedEffect
        }

        hasRequestedWeatherForCurrentLocation = true
        loadWeatherForCoordinates(
            coordinates.latitude,
            coordinates.longitude
        )

        isLocationFlowLoading = false
    }

    val shouldShowLoadingSpinner =
        isLoading || (weatherState.value == null && isLocationFlowLoading)

    if (shouldShowLoadingSpinner) {
        LoadingScreen()
        return
    }

    DetailedWeatherContent(
        weatherState = weatherState,
        onRefresh = {
            coroutineScope.launch {
                if (!locationPermissionController.hasLocationPermission) {
                    isLocationFlowLoading = true
                    locationPermissionController.requestLocationPermission()
                    return@launch
                }

                isLocationFlowLoading = true
                hasRequestedWeatherForCurrentLocation = false

                val coordinates = locationCoordinatesProvider.getCurrentCoordinatesOrNull()
                if (coordinates == null) {
                    isLocationFlowLoading = true
                    return@launch
                }

                loadWeatherForCoordinates(
                    coordinates.latitude,
                    coordinates.longitude
                )

                isLocationFlowLoading = false
            }
        }
    )
}