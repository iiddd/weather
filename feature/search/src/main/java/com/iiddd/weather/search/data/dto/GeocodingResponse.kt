package com.iiddd.weather.search.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeocodingResponse(
    val results: List<GeocodingResult> = emptyList(),
    val status: String? = null
)

@Serializable
data class GeocodingResult(
    @SerialName("formatted_address") val formattedAddress: String? = null,
    val geometry: Geometry? = null
)

@Serializable
data class Geometry(val location: LocationDto? = null)

@Serializable
data class LocationDto(val lat: Double, val lng: Double)