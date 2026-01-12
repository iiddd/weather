package com.iiddd.weather.forecast.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.core.network.ApiResult
import com.iiddd.weather.core.utils.coroutines.DefaultDispatcherProvider
import com.iiddd.weather.core.utils.coroutines.DispatcherProvider
import com.iiddd.weather.forecast.domain.location.CityNameResolver
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForecastViewModel(
    private val weatherRepository: WeatherRepository,
    private val cityNameResolver: CityNameResolver,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider(),
) : ViewModel() {

    private val _weather = MutableStateFlow<Weather?>(null)
    val weather: StateFlow<Weather?> = _weather

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<ApiError?>(null)
    val error: StateFlow<ApiError?> = _error

    fun loadWeather(
        latitude: Double,
        longitude: Double,
        cityOverride: String? = null
    ) {
        viewModelScope.launch(context = dispatcherProvider.main) {
            _isLoading.value = true
            _error.value = null

            try {
                val (result, resolvedCity) = withContext(context = dispatcherProvider.io) {
                    val weatherResult = weatherRepository.getWeather(
                        latitude = latitude,
                        longitude = longitude
                    )

                    val city = cityOverride ?: cityNameResolver.resolveCityName(
                        latitude = latitude,
                        longitude = longitude
                    )

                    weatherResult to city
                }

                when (result) {
                    is ApiResult.Success -> {
                        val value = result.value
                        _weather.value = if (resolvedCity != null) {
                            value.copy(city = resolvedCity)
                        } else {
                            value
                        }
                        _error.value = null
                    }

                    is ApiResult.Failure -> {
                        _weather.value = null
                        _error.value = result.error
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}