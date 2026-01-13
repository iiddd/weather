package com.iiddd.weather.location.domain

data class Coordinates(val latitude: Double, val longitude: Double)

interface LocationTracker {
    suspend fun getLastKnownLocation(): Coordinates?
}