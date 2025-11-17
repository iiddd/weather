package com.iiddd.weather.forecast.presentation.viewmodel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiddd.weather.core.utils.coroutines.DefaultDispatcherProvider
import com.iiddd.weather.core.utils.coroutines.DispatcherProvider
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import kotlin.coroutines.resume

class ForecastViewModel(
    private val weatherRepository: WeatherRepository,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    private val _weather = MutableStateFlow<Weather?>(null)
    val weather: StateFlow<Weather?> = _weather

    fun loadWeather(lat: Double, lon: Double, city: String? = null) {
        viewModelScope.launch(dispatcherProvider.main) {
            try {
                val result = withContext(dispatcherProvider.io) {
                    weatherRepository.getWeather(latitude = lat, longitude = lon)
                }
                _weather.value = if (city != null) result.copy(city = city) else result
            } catch (_: Exception) {
                _weather.value = null
            }
        }
    }

    fun loadWeatherWithGeocoding(
        context: Context,
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.launch(dispatcherProvider.main) {
            try {
                val weather = withContext(dispatcherProvider.io) {
                    weatherRepository.getWeather(latitude = latitude, longitude = longitude)
                }
                val city = withContext(dispatcherProvider.io) {
                    resolveCityName(context, latitude, longitude)
                }
                _weather.value = if (city != null) weather.copy(city = city) else weather
            } catch (_: Exception) {
                _weather.value = null
            }
        }
    }

    suspend fun resolveCityName(
        context: Context,
        latitude: Double,
        longitude: Double
    ): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())

            val addresses = geocoder.getFromLocationCompat(latitude, longitude)
            val address = addresses?.firstOrNull() ?: return null

            address.locality
                ?: address.subAdminArea
                ?: address.adminArea
        } catch (_: Exception) {
            null
        }
    }
}

@Suppress("DEPRECATION")
// Compatibility function for Geocoder.getFromLocation
suspend fun Geocoder.getFromLocationCompat(
    latitude: Double,
    longitude: Double,
    maxResults: Int = 1
): List<Address>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // New async API
        suspendCancellableCoroutine { continuation ->
            getFromLocation(latitude, longitude, maxResults) { result ->
                if (continuation.isActive) {
                    continuation.resume(result)
                }
            }
        }
    } else {
        // Old blocking API
        withContext(Dispatchers.IO) {
            try {
                getFromLocation(latitude, longitude, maxResults)
            } catch (_: IOException) {
                null
            }
        }
    }
}