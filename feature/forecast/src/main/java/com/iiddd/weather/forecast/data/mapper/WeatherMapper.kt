package com.iiddd.weather.forecast.data.mapper

import com.iiddd.weather.forecast.data.dto.OpenWeatherOneCallResponse
import com.iiddd.weather.forecast.domain.model.DailyForecast
import com.iiddd.weather.forecast.domain.model.HourlyForecast
import com.iiddd.weather.forecast.domain.model.Weather
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

private const val HOURLY_FORECAST_LIMIT = 8
private const val DAILY_FORECAST_LIMIT = 8

fun OpenWeatherOneCallResponse.toDomain(): Weather {

    return Weather(
        currentTemp = current.temp.roundToInt(),
        description = current.weather.firstOrNull()?.description ?: "",
        hourly = hourly.take(HOURLY_FORECAST_LIMIT).map {
            HourlyForecast(
                time = timeFormatter.format(Instant.ofEpochSecond(it.dt)),
                temp = it.temp.roundToInt(),
                icon = it.weather.firstOrNull()?.icon ?: ""
            )
        },
        daily = daily.take(DAILY_FORECAST_LIMIT).map {
            DailyForecast(
                day = dateFormatter.format(Instant.ofEpochSecond(it.dt)),
                tempDay = it.temp.day.roundToInt(),
                tempNight = it.temp.night.roundToInt(),
                icon = it.weather.firstOrNull()?.icon ?: ""
            )
        }
    )
}

private val timeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())

private val dateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("EEE").withZone(ZoneId.systemDefault())