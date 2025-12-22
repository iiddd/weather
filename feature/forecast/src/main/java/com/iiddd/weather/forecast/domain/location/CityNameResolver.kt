package com.iiddd.weather.forecast.domain.location

interface CityNameResolver {
    suspend fun resolveCityName(
        latitude: Double,
        longitude: Double
    ): String?
}