package com.iiddd.weather.forecast.domain.model

data class Weather(
    val currentTemp: Int,
    val description: String,
    val hourly: List<HourlyForecast> = emptyList(),
    val daily: List<DailyForecast> = emptyList(),
    val city: String? = null
)

data class HourlyForecast(
    val time: String,
    val temp: Int,
    val icon: String
)

data class DailyForecast(
    val day: String,
    val tempDay: Int,
    val tempNight: Int,
    val icon: String
)