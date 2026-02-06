package com.iiddd.weather.search.data

import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.core.network.ApiResult
import com.iiddd.weather.core.utils.coroutines.DefaultDispatcherProvider
import com.iiddd.weather.core.utils.coroutines.DispatcherProvider
import com.iiddd.weather.location.domain.GeocodingService
import com.iiddd.weather.search.domain.Location
import com.iiddd.weather.search.domain.SearchRepository
import kotlinx.coroutines.withContext

class SearchRepositoryImpl(
    private val geocodingService: GeocodingService,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider(),
) : SearchRepository {

    override suspend fun searchLocation(
        query: String,
        maxResults: Int
    ): ApiResult<List<Location>> =
        withContext(context = dispatcherProvider.io) {
            try {
                val geocodedLocations = geocodingService.forwardGeocode(
                    query = query,
                    maxResults = maxResults,
                )

                val locations = geocodedLocations.map { geocodedLocation ->
                    Location(
                        lat = geocodedLocation.latitude,
                        lon = geocodedLocation.longitude,
                        name = geocodedLocation.formattedAddress,
                    )
                }

                if (locations.isEmpty()) {
                    ApiResult.Failure(
                        error = ApiError.Input(message = "No results found for '$query'")
                    )
                } else {
                    ApiResult.Success(value = locations)
                }
            } catch (exception: Exception) {
                ApiResult.Failure(
                    error = ApiError.Network(message = exception.message ?: "Unknown error")
                )
            }
        }
}