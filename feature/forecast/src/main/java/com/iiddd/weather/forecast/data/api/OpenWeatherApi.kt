package com.iiddd.weather.forecast.data.api

import com.iiddd.weather.forecast.data.dto.OpenWeatherOneCallResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("data/3.0/onecall")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String = "minutely,alerts",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en",
        @Query("appid") apiKey: String
    ): OpenWeatherOneCallResponse
}