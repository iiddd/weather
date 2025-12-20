package com.iiddd.weather.ui.navigation

sealed interface Destination {
    data object Home : Destination
    data object Search : Destination
    data object Settings : Destination

    data class Details(
        val latitude: Double,
        val longitude: Double
    ) : Destination
}