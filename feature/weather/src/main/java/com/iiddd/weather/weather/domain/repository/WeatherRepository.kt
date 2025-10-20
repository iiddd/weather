package com.iiddd.weather.weather.domain.repository

import com.iiddd.weather.weather.domain.model.WeatherByCity

interface WeatherRepository {

    suspend fun getWeatherByCityName(city: String): WeatherByCity
}