package com.iiddd.weather.weather.data.api

import com.iiddd.weather.weather.data.dto.OpenWeatherByCityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): OpenWeatherByCityResponse
}