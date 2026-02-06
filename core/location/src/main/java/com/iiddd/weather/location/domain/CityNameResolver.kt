package com.iiddd.weather.location.domain

interface CityNameResolver {
    suspend fun resolveCityName(
        latitude: Double,
        longitude: Double
    ): String?
}

