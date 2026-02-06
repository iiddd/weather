package com.iiddd.weather.location.domain

data class GeocodedLocation(
    val latitude: Double,
    val longitude: Double,
    val formattedAddress: String,
)

interface GeocodingService {

    suspend fun forwardGeocode(
        query: String,
        maxResults: Int = 1,
    ): List<GeocodedLocation>

    suspend fun reverseGeocode(
        latitude: Double,
        longitude: Double,
    ): String?
}

