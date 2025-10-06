package com.iiddd.weather.home.data.repository

import com.iiddd.weather.home.data.api.OpenWeatherApiService
import com.iiddd.weather.home.data.mapper.toDomain
import com.iiddd.weather.home.domain.model.WeatherByCity
import com.iiddd.weather.home.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val api: OpenWeatherApiService
) : WeatherRepository {

    val API_KEY = "REMOVED_KEY"

    override suspend fun getWeatherByCityName(city: String): WeatherByCity {
        val response = api.getWeather(city, API_KEY)
        return response.toDomain()
    }
}