package com.iiddd.weather.location.data

import com.iiddd.weather.location.data.api.GeocodingApi
import com.iiddd.weather.location.data.dto.GeocodingResult
import com.iiddd.weather.location.domain.GeocodedLocation
import com.iiddd.weather.location.domain.GeocodingService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoogleGeocodingService(
    private val geocodingApi: GeocodingApi,
    private val apiKey: String,
) : GeocodingService {

    override suspend fun forwardGeocode(
        query: String,
        maxResults: Int,
    ): List<GeocodedLocation> = withContext(context = Dispatchers.IO) {
        try {
            val response = geocodingApi.forwardGeocode(
                address = query,
                apiKey = apiKey,
                language = "en",
            )

            response.results
                .take(maxResults)
                .mapNotNull { result ->
                    val location = result.geometry?.location ?: return@mapNotNull null
                    val formattedAddress = formatCityCountry(result = result)
                        ?: result.formattedAddress
                        ?: return@mapNotNull null

                    GeocodedLocation(
                        latitude = location.lat,
                        longitude = location.lng,
                        formattedAddress = formattedAddress,
                    )
                }
        } catch (exception: Exception) {
            emptyList()
        }
    }

    override suspend fun reverseGeocode(
        latitude: Double,
        longitude: Double,
    ): String? = withContext(context = Dispatchers.IO) {
        try {
            val response = geocodingApi.reverseGeocode(
                latitudeLongitude = "$latitude,$longitude",
                apiKey = apiKey,
                language = "en",
            )

            val result = response.results.firstOrNull() ?: return@withContext null
            formatCityCountry(result = result) ?: result.formattedAddress
        } catch (exception: Exception) {
            null
        }
    }

    private fun formatCityCountry(result: GeocodingResult): String? {
        val addressComponents = result.addressComponents

        val locality = addressComponents.find { component ->
            component.types.contains(ADDRESS_TYPE_LOCALITY)
        }?.longName

        val administrativeArea = addressComponents.find { component ->
            component.types.contains(ADDRESS_TYPE_ADMINISTRATIVE_AREA_LEVEL_1)
        }?.longName

        val country = addressComponents.find { component ->
            component.types.contains(ADDRESS_TYPE_COUNTRY)
        }?.longName

        val cityName = locality ?: administrativeArea

        return when {
            cityName != null && country != null -> "$cityName, $country"
            cityName != null -> cityName
            country != null -> country
            else -> null
        }
    }

    private companion object {
        const val ADDRESS_TYPE_LOCALITY = "locality"
        const val ADDRESS_TYPE_ADMINISTRATIVE_AREA_LEVEL_1 = "administrative_area_level_1"
        const val ADDRESS_TYPE_COUNTRY = "country"
    }
}


