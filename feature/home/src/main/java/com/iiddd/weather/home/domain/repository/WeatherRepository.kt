package com.iiddd.weather.home.domain.repository

import com.iiddd.weather.home.domain.model.WeatherByCity

interface WeatherRepository {

    suspend fun getWeatherByCityName(city: String): WeatherByCity
}