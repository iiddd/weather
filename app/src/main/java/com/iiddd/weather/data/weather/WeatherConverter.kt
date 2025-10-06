package com.iiddd.weather.data.weather

import com.iiddd.weather.domain.weather.WeatherByCity

fun OpenWeatherByCityResponse.toDomain() = WeatherByCity(
    degree = main?.temp.toString(),
    condition = base.toString()
)