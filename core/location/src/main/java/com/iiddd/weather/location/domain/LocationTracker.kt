package com.iiddd.weather.location.domain

data class Coordinate(val latitude: Double, val longitude: Double)

interface LocationTracker {
    suspend fun getLastKnownLocation(): Coordinate?
}