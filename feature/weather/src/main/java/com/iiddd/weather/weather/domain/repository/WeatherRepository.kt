package com.iiddd.weather.weather.domain.repository

import com.iiddd.weather.weather.domain.model.Weather

interface WeatherRepository {

    suspend fun getWeather(lat: Double, lon: Double): Weather
}