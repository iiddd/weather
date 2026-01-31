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
                HourlyForecast(time = "10:00", temp = 14, icon = "01d"),
                HourlyForecast(time = "11:00", temp = 15, icon = "02d"),
                HourlyForecast(time = "12:00", temp = 16, icon = "02d"),
                HourlyForecast(time = "13:00", temp = 17, icon = "03d"),
                HourlyForecast(time = "14:00", temp = 18, icon = "04d"),
                HourlyForecast(time = "15:00", temp = 17, icon = "04d"),
            ),
            daily = listOf(
                DailyForecast(day = "Monday", tempDay = 16, tempNight = 8, icon = "01d"),
                DailyForecast(day = "Tuesday", tempDay = 17, tempNight = 9, icon = "02d"),
                DailyForecast(day = "Wednesday", tempDay = 15, tempNight = 7, icon = "03d"),
                DailyForecast(day = "Thursday", tempDay = 18, tempNight = 10, icon = "04d"),
                DailyForecast(day = "Friday", tempDay = 20, tempNight = 12, icon = "09d"),
                DailyForecast(day = "Saturday", tempDay = 19, tempNight = 11, icon = "10d"),
                DailyForecast(day = "Sunday", tempDay = 21, tempNight = 13, icon = "01d"),
            ),
        )
    }

    /**
     * Convenience property returning a fresh sample Weather instance.
     * Access via PreviewWeatherProvider.sampleWeather
     */
    val sampleWeather: Weather
        get() = createSampleWeather()
}