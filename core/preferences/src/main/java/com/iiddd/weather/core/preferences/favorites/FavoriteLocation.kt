package com.iiddd.weather.core.preferences.favorites

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteLocation(
    val id: String,
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
)

