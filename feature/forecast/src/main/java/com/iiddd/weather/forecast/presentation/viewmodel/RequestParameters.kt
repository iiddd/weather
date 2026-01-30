package com.iiddd.weather.forecast.presentation.viewmodel

internal data class RequestParameters(
    val latitude: Double?,
    val longitude: Double?,
    val useDeviceLocation: Boolean,
)