package com.iiddd.weather.weather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiddd.weather.weather.domain.model.Weather
import com.iiddd.weather.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailedScreenViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weather = MutableStateFlow<Weather?>(null)
    val weather: StateFlow<Weather?> = _weather

    fun loadWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val result = weatherRepository.getWeather(lat = lat, lon = lon)
                _weather.value = result
            } catch (_: Exception) {
                _weather.value = null
            }
        }
    }
}