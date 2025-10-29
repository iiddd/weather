package com.iiddd.weather.search.domain

data class Location(
    val lat: Double,
    val lon: Double,
    val name: String? = null
)