package com.iiddd.weather.ui.navigation

sealed interface Destination {

    data object Search : Destination

    data object Settings : Destination

    data class Weather(
        val latitude: Double? = null,
        val longitude: Double? = null
    ) : Destination
}