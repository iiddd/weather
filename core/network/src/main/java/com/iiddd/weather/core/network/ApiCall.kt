package com.iiddd.weather.core.network

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException
import kotlinx.serialization.SerializationException

/**
 * Centralized wrapper for any API/service call + mapping.
 * Put both network call and mapping inside [block] so mapping failures are also normalized.
 */
suspend inline fun <T> apiCall(
    timeoutMilliseconds: Long = 15_000L,
    crossinline block: suspend () -> T
): ApiResult<T> {
    return try {
        val value = withContext(context = Dispatchers.IO) {
            withTimeout(timeMillis = timeoutMilliseconds) {
                block()
            }
        }
        ApiResult.Success(value)
    } catch (e: TimeoutCancellationException) {
        ApiResult.Failure(
            error = ApiError.Timeout(message = e.message)
        )
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        ApiResult.Failure(
            error = ApiError.Http(
                code = e.code(),
                message = e.message(),
                body = e.response()?.errorBody()?.string()
            )
        )
    } catch (e: IOException) {
        ApiResult.Failure(error = ApiError.Network(message = e.message))
    } catch (e: SerializationException) {
        ApiResult.Failure(error = ApiError.Serialization(message = e.message))
    } catch (e: IllegalArgumentException) {
        ApiResult.Failure(error = ApiError.Mapping(message = e.message))
    } catch (e: IllegalStateException) {
        ApiResult.Failure(error = ApiError.Mapping(message = e.message))
    } catch (e: Exception) {
        ApiResult.Failure(error = ApiError.Unknown(message = e.message))
    }
}