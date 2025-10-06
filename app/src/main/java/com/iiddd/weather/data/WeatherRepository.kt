package com.iiddd.weather.data

import com.iiddd.weather.domain.weather.WeatherByCity

interface WeatherRepository {

    suspend fun getWeatherByCityName(city: String): WeatherByCity
}