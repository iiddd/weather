package com.iiddd.weather.search.data

import android.util.Log
import com.iiddd.weather.search.data.api.GeocodingApi
import com.iiddd.weather.search.domain.Location
import com.iiddd.weather.search.domain.SearchRepository

class SearchRepositoryImpl(
    private val geocodingApi: GeocodingApi,
    private val apiKey: String
) : SearchRepository {
    override suspend fun searchLocation(query: String, maxResults: Int): List<Location> {
        try {
            val resp = geocodingApi.geocode(query, apiKey, "en")
            return resp.results
                .take(maxResults)
                .mapNotNull { r ->
                    val loc = r.geometry?.location ?: return@mapNotNull null
                    Location(
                        lat = loc.lat,
                        lon = loc.lng,
                        name = r.formattedAddress ?: query
                    )
                }
        } catch (e: Exception) {
            Log.e("SearchRepository", "geocode error for query='$query'", e)
            throw e
        }
    }
}