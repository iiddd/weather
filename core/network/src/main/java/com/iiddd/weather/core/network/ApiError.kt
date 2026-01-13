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

    data class Timeout(
        val message: String? = null
    ) : ApiError

    /**
     * App-level validation / precondition failures (not a server/network failure).
     * Examples: missing coordinates, invalid user input, unsupported state.
     */
    data class Input(
        val message: String? = null
    ) : ApiError

    data class Unknown(val message: String? = null) : ApiError
}

fun ApiError.toUiMessage(): String =
    when (this) {
        is ApiError.Timeout -> "Request timed out. Please try again."
        is ApiError.Network -> "Network error. Check your connection and retry."
        is ApiError.Http -> "Server error (`${code}`). Please try again."
        is ApiError.Serialization -> "Response parsing error. Please try again."
        is ApiError.Mapping -> "Unexpected data error. Please try again."
        is ApiError.Input -> message ?: "Invalid input. Please try again."
        is ApiError.Unknown -> "Unknown error. Please try again."
    }