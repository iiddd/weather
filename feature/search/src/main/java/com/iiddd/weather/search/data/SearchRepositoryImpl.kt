package com.iiddd.weather.search.data

import com.iiddd.weather.core.network.ApiResult.Failure
import com.iiddd.weather.core.network.ApiResult.Success
import com.iiddd.weather.core.network.apiCall
import com.iiddd.weather.core.utils.coroutines.DefaultDispatcherProvider
import com.iiddd.weather.core.utils.coroutines.DispatcherProvider
import com.iiddd.weather.search.data.api.GeocodingApi
import com.iiddd.weather.search.domain.Location
import com.iiddd.weather.search.domain.SearchRepository
import kotlinx.coroutines.withContext

class SearchRepositoryImpl(
    private val geocodingApi: GeocodingApi,
    private val apiKey: String,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider(),
) : SearchRepository {

    override suspend fun searchLocation(query: String, maxResults: Int): List<Location> =
        withContext(dispatcherProvider.io) {
            apiCall {
                geocodingApi.geocode(query, apiKey, "en")
            }.let { result ->
                when (result) {
                    is Success -> {
                        result.value.results
                            .take(maxResults)
                            .mapNotNull { r ->
                                val loc = r.geometry?.location ?: return@mapNotNull null
                                Location(
                                    lat = loc.lat,
                                    lon = loc.lng,
                                    name = r.formattedAddress ?: query
                                )
                            }
                    }

                    is Failure -> {
                        throw RuntimeException(result.error.toString())
                    }
                }
            }
        }
}