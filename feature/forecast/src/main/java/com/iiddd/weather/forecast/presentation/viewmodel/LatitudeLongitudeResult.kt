package com.iiddd.weather.forecast.presentation.viewmodel

import com.iiddd.weather.core.network.ApiError

sealed interface LatitudeLongitudeResult {
    data class Success(
        val latitude: Double,
        val longitude: Double,
    ) : LatitudeLongitudeResult

    data class Failure(
        val apiError: ApiError,
    ) : LatitudeLongitudeResult
}
