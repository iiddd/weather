package com.iiddd.weather.location.data.api

import com.iiddd.weather.location.data.dto.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {

    @GET("geocode/json")
    suspend fun forwardGeocode(
        @Query("address") address: String,
        @Query("key") apiKey: String,
        @Query("language") language: String = "en"
    ): GeocodingResponse

    @GET("geocode/json")
    suspend fun reverseGeocode(
        @Query("latlng") latitudeLongitude: String,
        @Query("key") apiKey: String,
        @Query("language") language: String = "en"
    ): GeocodingResponse
}

