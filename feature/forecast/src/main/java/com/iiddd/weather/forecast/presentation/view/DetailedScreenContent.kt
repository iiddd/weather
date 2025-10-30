package com.iiddd.weather.forecast.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.forecast.domain.model.DailyForecast
import com.iiddd.weather.forecast.domain.model.HourlyForecast
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.presentation.view.component.WeatherView

@Composable
fun DetailedWeatherContent(
    weatherState: State<Weather?>,
    onRefresh: () -> Unit
) {
    WeatherView(
        weatherState = weatherState,
        onRefresh = onRefresh
    )
}

@Preview(showBackground = true)
@Composable
fun DetailedWeatherContentPreview() {
    val mockState = remember {
        mutableStateOf(
            Weather(
                currentTemp = 13.0,
                description = "Clear",
                hourly = listOf(
                    HourlyForecast(time = "09:00", temp = 13.0, icon = "01d"),
                    HourlyForecast(time = "12:00", temp = 15.0, icon = "02d")
                ),
                daily = listOf(
                    DailyForecast(day = "Mon", tempDay = 16.0, tempNight = 8.0, icon = "01d"),
                    DailyForecast(day = "Tue", tempDay = 17.0, tempNight = 9.0, icon = "02d")
                )
            )
        )
    }

    WeatherTheme {
        DetailedWeatherContent(
            weatherState = mockState,
            onRefresh = {}
        )
    }
}