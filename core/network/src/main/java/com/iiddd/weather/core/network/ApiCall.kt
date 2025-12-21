package com.iiddd.weather.core.network

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import kotlinx.serialization.SerializationException

/**
 * Centralized wrapper for any API/service call \+ mapping.
 * Put both network call and mapping inside [block] so mapping failures are also normalized.
 */
suspend inline fun <T> apiCall(
    crossinline block: suspend () -> T
): ApiResult<T> {
    return try {
        val value = withContext(Dispatchers.IO) { block() }
        ApiResult.Success(value)
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        ApiResult.Failure(
            ApiError.Http(
                code = e.code(),
                message = e.message(),
                body = e.response()?.errorBody()?.string()
            )
        )
    } catch (e: IOException) {
        ApiResult.Failure(ApiError.Network(message = e.message))
    } catch (e: SerializationException) {
        ApiResult.Failure(ApiError.Serialization(message = e.message))
    } catch (e: IllegalArgumentException) {
        ApiResult.Failure(ApiError.Mapping(message = e.message))
    } catch (e: IllegalStateException) {
        ApiResult.Failure(ApiError.Mapping(message = e.message))
    } catch (e: Exception) {
        ApiResult.Failure(ApiError.Unknown(message = e.message))
    }
}