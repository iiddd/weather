package com.iiddd.weather.forecast.data.repository


import com.iiddd.weather.forecast.BuildConfig
import com.iiddd.weather.forecast.data.api.OpenWeatherApi
import com.iiddd.weather.forecast.data.mapper.toDomain
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.domain.repository.WeatherRepository

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