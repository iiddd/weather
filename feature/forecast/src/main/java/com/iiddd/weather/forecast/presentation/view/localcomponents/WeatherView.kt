package com.iiddd.weather.forecast.presentation.view.localcomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iiddd.weather.forecast.domain.model.DailyForecast
import com.iiddd.weather.forecast.domain.model.HourlyForecast
import com.iiddd.weather.forecast.domain.model.Weather

@Composable
fun WeatherView(
    weatherState: State<Weather?>,
    onRefresh: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HeaderView(onRefresh)
            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(16.dp))
            CurrentWeatherView(weatherState.value)
            ForecastView(weatherState.value)
        }
    }
}

@Preview
@Composable
fun WeatherViewPreview() {
    val mockWeather: State<Weather?> = remember {
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

    WeatherView(weatherState = mockWeather)
}