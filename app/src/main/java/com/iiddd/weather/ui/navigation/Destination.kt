package com.iiddd.weather.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    @Serializable
    data class Weather(
        val latitude: Double? = null,
        val longitude: Double? = null,
        val useDeviceLocation: Boolean = latitude == null && longitude == null,
    ) : Destination

    @Serializable
    data object Search : Destination

    @Serializable
    data object Settings : Destination
}