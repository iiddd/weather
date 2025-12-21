package com.iiddd.weather.core.network

sealed interface ApiError {
    data class Http(
        val code: Int,
        val message: String? = null,
        val body: String? = null
    ) : ApiError

    data class Network(val message: String? = null) : ApiError
    data class Serialization(val message: String? = null) : ApiError
    data class Mapping(val message: String? = null) : ApiError
    data class Unknown(val message: String? = null) : ApiError
}