package com.iiddd.weather.location.data.dto

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
    @SerialName("address_components") val addressComponents: List<AddressComponent> = emptyList(),
    val geometry: Geometry? = null
)

@Serializable
data class AddressComponent(
    @SerialName("long_name") val longName: String? = null,
    @SerialName("short_name") val shortName: String? = null,
    val types: List<String> = emptyList()
)

@Serializable
data class Geometry(val location: LocationDto? = null)

@Serializable
data class LocationDto(val lat: Double, val lng: Double)

