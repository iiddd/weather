package com.iiddd.weather.weather.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class OpenWeatherOneCallResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val current: Current,
    val hourly: List<Hourly>,
    val daily: List<Daily>
)

@Serializable
data class Current(
    val dt: Long,
    val temp: Double,
    val humidity: Int,
    val weather: List<WeatherDescription>
)

@Serializable
data class Hourly(
    val dt: Long,
    val temp: Double,
    val weather: List<WeatherDescription>
)

@Serializable
data class Daily(
    val dt: Long,
    val temp: Temp,
    val weather: List<WeatherDescription>
)

@Serializable
data class Temp(val day: Double, val night: Double)

@Serializable
data class WeatherDescription(
    val main: String,
    val description: String,
    val icon: String
)