package com.iiddd.weather.forecast.domain.repository

import com.iiddd.weather.forecast.domain.model.Weather

interface WeatherRepository {

    suspend fun getWeather(lat: Double, lon: Double): Weather
}