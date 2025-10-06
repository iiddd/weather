package com.iiddd.weather.data

import com.iiddd.weather.data.retrofit.OpenWeatherApiService
import com.iiddd.weather.domain.weather.WeatherByCity
import com.iiddd.weather.data.weather.toDomain // This should map OpenWeatherByCityResponse -> WeatherByCity
import javax.inject.Inject

class OpenWeatherClient @Inject constructor(
    private val api: OpenWeatherApiService
) : WeatherRepository {

    val API_KEY = "REMOVED_KEY"

    override suspend fun getWeatherByCityName(city: String): WeatherByCity {
        val response = api.getWeather(city, API_KEY)
        return response.toDomain()
    }
}