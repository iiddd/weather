package com.iiddd.weather.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiddd.weather.home.domain.model.WeatherByCity
import com.iiddd.weather.home.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weather = MutableStateFlow<WeatherByCity?>(null)
    val weather: StateFlow<WeatherByCity?> = _weather

    fun loadWeather(city: String) {
        viewModelScope.launch {
            try {
                val result = weatherRepository.getWeatherByCityName(city)
                _weather.value = result
            } catch (_: Exception) {
                _weather.value = null
            }
        }
    }
}