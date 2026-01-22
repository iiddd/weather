package com.iiddd.weather.forecast.presentation.previewfixtures

import com.iiddd.weather.forecast.domain.model.DailyForecast
import com.iiddd.weather.forecast.domain.model.HourlyForecast
import com.iiddd.weather.forecast.domain.model.Weather

object PreviewWeatherProvider {

    /**
     * Returns a new sample Weather instance for previews and tests.
     * Use named arguments when calling constructors to follow project conventions.
     */
    fun createSampleWeather(): Weather {
        return Weather(
            currentTemp = 13,
            description = "Clear",
            hourly = listOf(
                HourlyForecast(time = "09:00", temp = 13, icon = "01d"),
                HourlyForecast(time = "12:00", temp = 15, icon = "02d")
            ),
            daily = listOf(
                DailyForecast(day = "Mon", tempDay = 16, tempNight = 8, icon = "01d"),
                DailyForecast(day = "Tue", tempDay = 17, tempNight = 9, icon = "02d")
            )
        )
    }

    /**
     * Convenience property returning a fresh sample Weather instance.
     * Access via PreviewWeatherProvider.sampleWeather
     */
    val sampleWeather: Weather
        get() = createSampleWeather()
}