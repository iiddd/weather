package com.iiddd.weather.weather.data.repository

import com.iiddd.weather.weather.data.api.OpenWeatherApiService
import com.iiddd.weather.weather.data.mapper.toDomain
import com.iiddd.weather.weather.domain.model.WeatherByCity
import com.iiddd.weather.weather.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val api: OpenWeatherApiService
) : WeatherRepository {

    val API_KEY = "REMOVED_KEY"

    override suspend fun getWeatherByCityName(city: String): WeatherByCity {
        val response = api.getWeather(city, API_KEY)
        return response.toDomain()
    }
}