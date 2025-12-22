package com.iiddd.weather.forecast.presentation.view.permission

class LocationPermissionState internal constructor(
    private val hasLocationPermissionProvider: () -> Boolean,
    private val shouldShowPermissionRationaleDialogProvider: () -> Boolean,
    private val shouldShowOpenSettingsDialogProvider: () -> Boolean,
    private val requestLocationPermissionInternal: () -> Unit
) {
    val hasLocationPermission: Boolean
        get() = hasLocationPermissionProvider()

    val shouldShowPermissionRationaleDialog: Boolean
        get() = shouldShowPermissionRationaleDialogProvider()

    val shouldShowOpenSettingsDialog: Boolean
        get() = shouldShowOpenSettingsDialogProvider()

    fun requestLocationPermission() {
        requestLocationPermissionInternal.invoke()
    }
}