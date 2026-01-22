package com.iiddd.weather.forecast.presentation.logic

internal data class WeatherLoadInput(
    val latitude: Double?,
    val longitude: Double?,
    val hasLocationPermission: Boolean,
    val shouldRequestDeviceLocation: Boolean,
    val isContentVisible: Boolean,
) {
    val isNavigationCoordinatesProvided: Boolean
        get() = latitude != null && longitude != null

    val shouldLoadWeather: Boolean
        get() =
            isNavigationCoordinatesProvided ||
                    (shouldRequestDeviceLocation &&
                            hasLocationPermission &&
                            !isContentVisible)
}