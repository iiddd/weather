package com.iiddd.weather.forecast.data.mapper

import com.iiddd.weather.forecast.data.dto.Current
import com.iiddd.weather.forecast.data.dto.Daily
import com.iiddd.weather.forecast.data.dto.Hourly
import com.iiddd.weather.forecast.data.dto.OpenWeatherOneCallResponse
import com.iiddd.weather.forecast.data.dto.Temp
import com.iiddd.weather.forecast.data.dto.WeatherDescription
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class WeatherMapperTest {

    private val timeFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())

    private val dateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("EEE").withZone(ZoneId.systemDefault())

    @Test
    fun `toDomain maps current temperature correctly`() {
        val response = createResponse(currentTemp = 25.7)

        val result = response.toDomain()

        assertEquals(26, result.currentTemp)
    }

    @Test
    fun `toDomain maps current temperature with rounding down`() {
        val response = createResponse(currentTemp = 25.2)

        val result = response.toDomain()

        assertEquals(25, result.currentTemp)
    }

    @Test
    fun `toDomain maps current description from first weather item`() {
        val weatherDescription = WeatherDescription(
            main = "Clouds",
            description = "scattered clouds",
            icon = "03d",
        )
        val response = createResponse(weatherDescriptions = listOf(weatherDescription))

        val result = response.toDomain()

        assertEquals("scattered clouds", result.description)
    }

    @Test
    fun `toDomain returns empty description when no weather items`() {
        val response = createResponse(weatherDescriptions = emptyList())

        val result = response.toDomain()

        assertEquals("", result.description)
    }

    @Test
    fun `toDomain maps hourly forecast correctly`() {
        val timestamp = 1700000000L
        val hourlyList = listOf(
            Hourly(
                dt = timestamp,
                temp = 22.8,
                weather = listOf(
                    WeatherDescription(
                        main = "Clear",
                        description = "clear sky",
                        icon = "01d",
                    )
                )
            )
        )
        val response = createResponse(hourlyList = hourlyList)

        val result = response.toDomain()

        assertEquals(1, result.hourly.size)
        val hourlyForecast = result.hourly.first()
        assertEquals(23, hourlyForecast.temp)
        assertEquals("01d", hourlyForecast.icon)
        assertEquals(
            timeFormatter.format(Instant.ofEpochSecond(timestamp)),
            hourlyForecast.time,
        )
    }

    @Test
    fun `toDomain limits hourly forecast to 24 items`() {
        val hourlyList = (1..30).map { index ->
            Hourly(
                dt = 1700000000L + (index * 3600),
                temp = 20.0 + index,
                weather = listOf(
                    WeatherDescription(
                        main = "Clouds",
                        description = "cloudy",
                        icon = "03d",
                    )
                )
            )
        }
        val response = createResponse(hourlyList = hourlyList)

        val result = response.toDomain()

        assertEquals(24, result.hourly.size)
    }

    @Test
    fun `toDomain returns empty icon for hourly when weather list is empty`() {
        val hourlyList = listOf(
            Hourly(
                dt = 1700000000L,
                temp = 22.0,
                weather = emptyList(),
            )
        )
        val response = createResponse(hourlyList = hourlyList)

        val result = response.toDomain()

        assertEquals("", result.hourly.first().icon)
    }

    @Test
    fun `toDomain maps daily forecast correctly`() {
        val timestamp = 1700000000L
        val dailyList = listOf(
            Daily(
                dt = timestamp,
                temp = Temp(day = 28.6, night = 18.3),
                weather = listOf(
                    WeatherDescription(
                        main = "Rain",
                        description = "light rain",
                        icon = "10d",
                    )
                )
            )
        )
        val response = createResponse(dailyList = dailyList)

        val result = response.toDomain()

        assertEquals(1, result.daily.size)
        val dailyForecast = result.daily.first()
        assertEquals(29, dailyForecast.tempDay)
        assertEquals(18, dailyForecast.tempNight)
        assertEquals("10d", dailyForecast.icon)
        assertEquals(
            dateFormatter.format(Instant.ofEpochSecond(timestamp)),
            dailyForecast.day,
        )
    }

    @Test
    fun `toDomain limits daily forecast to 10 items`() {
        val dailyList = (1..15).map { index ->
            Daily(
                dt = 1700000000L + (index * 86400),
                temp = Temp(day = 25.0, night = 15.0),
                weather = listOf(
                    WeatherDescription(
                        main = "Clear",
                        description = "clear",
                        icon = "01d",
                    )
                )
            )
        }
        val response = createResponse(dailyList = dailyList)

        val result = response.toDomain()

        assertEquals(10, result.daily.size)
    }

    @Test
    fun `toDomain returns empty icon for daily when weather list is empty`() {
        val dailyList = listOf(
            Daily(
                dt = 1700000000L,
                temp = Temp(day = 25.0, night = 15.0),
                weather = emptyList(),
            )
        )
        val response = createResponse(dailyList = dailyList)

        val result = response.toDomain()

        assertEquals("", result.daily.first().icon)
    }

    private fun createResponse(
        currentTemp: Double = 20.0,
        weatherDescriptions: List<WeatherDescription> = listOf(
            WeatherDescription(
                main = "Clear",
                description = "clear sky",
                icon = "01d",
            )
        ),
        hourlyList: List<Hourly> = emptyList(),
        dailyList: List<Daily> = emptyList(),
    ): OpenWeatherOneCallResponse {
        return OpenWeatherOneCallResponse(
            lat = 52.0,
            lon = 5.0,
            timezone = "Europe/Amsterdam",
            current = Current(
                dt = 1700000000L,
                temp = currentTemp,
                humidity = 50,
                weather = weatherDescriptions,
            ),
            hourly = hourlyList,
            daily = dailyList,
        )
    }
}

