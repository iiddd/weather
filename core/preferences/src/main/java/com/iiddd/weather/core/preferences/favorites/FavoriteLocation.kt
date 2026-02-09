package com.iiddd.weather.core.preferences.favorites

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class FavoriteLocation(
    val id: String,
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
)
