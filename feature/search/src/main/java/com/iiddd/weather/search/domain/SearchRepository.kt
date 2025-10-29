package com.iiddd.weather.search.domain

interface SearchRepository {
    suspend fun searchLocation(query: String, maxResults: Int = 1): List<Location>
}