package com.iiddd.weather.ui.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiddd.weather.data.WeatherRepository
import com.iiddd.weather.domain.weather.WeatherByCity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weather = MutableStateFlow<WeatherByCity?>(null)
    val weather: StateFlow<WeatherByCity?> = _weather

    fun loadWeather(city: String) {
        viewModelScope.launch {
            try {
                val result = weatherRepository.getWeatherByCityName(city)
                _weather.value = result
            } catch (e: Exception) {
                // Handle error (optional: use a second flow for error)
                _weather.value = null
            }
        }
    }
}