package com.iiddd.weather.forecast.presentation.icons

import com.iiddd.weather.forecast.R as ForecastR

fun resolveWeatherIcon(iconCode: String): Int {
    return when (iconCode) {
        "01d" -> ForecastR.drawable.clear_sky_day
        "01n" -> ForecastR.drawable.clear_sky_night
        "02d" -> ForecastR.drawable.few_clouds_day
        "02n" -> ForecastR.drawable.few_clouds_night
        "03d", "03n" -> ForecastR.drawable.scattered_clouds_day
        "04d", "04n" -> ForecastR.drawable.broken_clouds_day
        "09d", "09n" -> ForecastR.drawable.shower_rain_day
        "10d" -> ForecastR.drawable.rain_day
        "10n" -> ForecastR.drawable.rain_night
        "11d", "11n" -> ForecastR.drawable.thunderstorm_day
        "13d", "13n" -> ForecastR.drawable.snow_day
        "50d", "50n" -> ForecastR.drawable.mist_day
        else -> ForecastR.drawable.air
    }
}