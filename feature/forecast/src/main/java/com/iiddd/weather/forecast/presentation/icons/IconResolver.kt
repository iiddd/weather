package com.iiddd.weather.forecast.presentation.icons

import com.iiddd.weather.forecast.R as ForecastR

fun resolveWeatherIcon(iconCode: String): Int {
    return when (iconCode) {
        "01d" -> ForecastR.drawable.day_clear_sky
        "01n" -> ForecastR.drawable.night_clear_sky
        "02d" -> ForecastR.drawable.day_few_clouds
        "02n" -> ForecastR.drawable.night_few_clouds
        "03d" -> ForecastR.drawable.day_scattered_clouds
        "03n" -> ForecastR.drawable.night_scattered_clouds
        "04d" -> ForecastR.drawable.day_broken_clouds
        "04n" -> ForecastR.drawable.night_broken_clouds
        "09d" -> ForecastR.drawable.day_shower_rain
        "09n" -> ForecastR.drawable.night_shower_rain
        "10d" -> ForecastR.drawable.day_rain
        "10n" -> ForecastR.drawable.night_rain
        "11d" -> ForecastR.drawable.day_thunderstorm
        "11n" -> ForecastR.drawable.night_thunderstorm
        "13d" -> ForecastR.drawable.day_snow
        "13n" -> ForecastR.drawable.night_snow
        "50d" -> ForecastR.drawable.day_mist
        "50n" -> ForecastR.drawable.night_mist
        else -> ForecastR.drawable.air
    }
}