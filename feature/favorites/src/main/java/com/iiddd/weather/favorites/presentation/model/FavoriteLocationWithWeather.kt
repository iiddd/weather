package com.iiddd.weather.favorites.presentation.model

import com.iiddd.weather.core.preferences.favorites.FavoriteLocation

data class FavoriteLocationWithWeather(
    val favoriteLocation: FavoriteLocation,
    val currentTemperature: Int? = null,
    val weatherDescription: String? = null,
    val weatherIcon: String? = null,
)
