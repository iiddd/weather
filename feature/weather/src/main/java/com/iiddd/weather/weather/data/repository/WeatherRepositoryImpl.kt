package com.iiddd.weather.weather.data.repository

import com.iiddd.weather.weather.BuildConfig
import com.iiddd.weather.weather.data.api.OpenWeatherApi
import com.iiddd.weather.weather.data.mapper.toDomain
import com.iiddd.weather.weather.domain.model.Weather
import com.iiddd.weather.weather.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val api: OpenWeatherApi,
) : WeatherRepository {

    private val apiKey = BuildConfig.OPEN_WEATHER_API_KEY

    override suspend fun getWeather(lat: Double, lon: Double): Weather {
        val response = api.getWeather(
            lat = lat,
            lon = lon,
            apiKey = apiKey,
        )
        return response.toDomain()
    }
}