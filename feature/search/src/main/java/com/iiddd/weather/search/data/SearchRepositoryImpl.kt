package com.iiddd.weather.search.data

import com.iiddd.weather.core.network.ApiResult
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

    override suspend fun searchLocation(
        query: String,
        maxResults: Int
    ): ApiResult<List<Location>> =
        withContext(context = dispatcherProvider.io) {
            apiCall {
                val response = geocodingApi.geocode(
                    address = query,
                    apiKey = apiKey,
                    language = "en"
                )

                response.results
                    .take(maxResults)
                    .mapNotNull { responseResult ->
                        val geometryLocation = responseResult.geometry?.location
                            ?: return@mapNotNull null

                        Location(
                            lat = geometryLocation.lat,
                            lon = geometryLocation.lng,
                            name = responseResult.formattedAddress ?: query
                        )
                    }
            }
        }
}