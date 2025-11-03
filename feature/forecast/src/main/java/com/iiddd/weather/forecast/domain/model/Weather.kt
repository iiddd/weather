package com.iiddd.weather.forecast.domain.model

data class Weather(
    val currentTemp: Double,
    val description: String,
    val hourly: List<HourlyForecast> = emptyList(),
    val daily: List<DailyForecast> = emptyList(),
    val city: String? = null
)

data class HourlyForecast(
    val time: String,
    val temp: Double,
    val icon: String
)

data class DailyForecast(
    val day: String,
    val tempDay: Double,
    val tempNight: Double,
    val icon: String
)