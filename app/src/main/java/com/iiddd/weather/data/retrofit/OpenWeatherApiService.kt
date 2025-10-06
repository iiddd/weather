package com.iiddd.weather.data.retrofit

import com.iiddd.weather.data.weather.OpenWeatherByCityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): OpenWeatherByCityResponse
}