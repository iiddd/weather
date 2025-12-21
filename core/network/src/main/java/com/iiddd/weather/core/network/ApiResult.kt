package com.iiddd.weather.core.network

sealed interface ApiResult<out T> {
    data class Success<T>(val value: T) : ApiResult<T>
    data class Failure(val error: ApiError) : ApiResult<Nothing>
}

fun <T> ApiResult<T>.getOrNull(): T? = (this as? ApiResult.Success)?.value