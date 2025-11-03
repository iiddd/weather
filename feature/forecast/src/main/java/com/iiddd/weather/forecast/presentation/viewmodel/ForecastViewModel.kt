package com.iiddd.weather.forecast.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weather = MutableStateFlow<Weather?>(null)
    val weather: StateFlow<Weather?> = _weather

    fun loadWeather(lat: Double, lon: Double, city: String? = null) {
        viewModelScope.launch {
            try {
                val result = weatherRepository.getWeather(lat = lat, lon = lon)
                _weather.value = if (city != null) result.copy(city = city) else result
            } catch (_: Exception) {
                _weather.value = null
            }
        }
    }
}