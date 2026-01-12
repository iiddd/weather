package com.iiddd.weather.search.domain

import com.iiddd.weather.core.network.ApiResult

interface SearchRepository {
    suspend fun searchLocation(
        query: String,
        maxResults: Int
    ): ApiResult<List<Location>>
}