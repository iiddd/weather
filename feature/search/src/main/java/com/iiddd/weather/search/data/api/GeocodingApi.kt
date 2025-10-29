package com.iiddd.weather.search.data.api

import com.iiddd.weather.search.data.dto.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {
    @GET("geocode/json")
    suspend fun geocode(
        @Query("address") address: String,
        @Query("key") apiKey: String,
        @Query("language") language: String = "en"
    ): GeocodingResponse
}