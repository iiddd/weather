package com.iiddd.weather.forecast.data.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.iiddd.weather.forecast.domain.location.CityNameResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import kotlin.coroutines.resume

class AndroidCityNameResolver(
    private val applicationContext: Context
) : CityNameResolver {

    override suspend fun resolveCityName(
        latitude: Double,
        longitude: Double
    ): String? {
        return try {
            val geocoder = Geocoder(applicationContext, Locale.getDefault())
            val addresses = geocoder.getFromLocationCompat(
                latitude = latitude,
                longitude = longitude,
                maxResults = 1
            )
            val address = addresses?.firstOrNull() ?: return null
            address.locality ?: address.subAdminArea ?: address.adminArea
        } catch (_: Exception) {
            null
        }
    }
}

@Suppress("DEPRECATION")
private suspend fun Geocoder.getFromLocationCompat(
    latitude: Double,
    longitude: Double,
    maxResults: Int
): List<Address>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        suspendCancellableCoroutine { continuation ->
            getFromLocation(latitude, longitude, maxResults) { result ->
                if (continuation.isActive) {
                    continuation.resume(result)
                }
            }
        }
    } else {
        withContext(context = Dispatchers.IO) {
            try {
                getFromLocation(latitude, longitude, maxResults)
            } catch (_: IOException) {
                null
            }
        }
    }
}