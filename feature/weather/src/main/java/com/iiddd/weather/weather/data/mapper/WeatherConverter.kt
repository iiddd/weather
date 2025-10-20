package com.iiddd.weather.weather.data.mapper

import com.iiddd.weather.weather.data.dto.OpenWeatherByCityResponse
import com.iiddd.weather.weather.domain.model.WeatherByCity

fun OpenWeatherByCityResponse.toDomain() = WeatherByCity(
    degree = main?.temp.toString(),
    condition = base.toString()
)