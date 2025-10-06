package com.iiddd.weather.home.data.mapper

import com.iiddd.weather.home.data.dto.OpenWeatherByCityResponse
import com.iiddd.weather.home.domain.model.WeatherByCity

fun OpenWeatherByCityResponse.toDomain() = WeatherByCity(
    degree = main?.temp.toString(),
    condition = base.toString()
)