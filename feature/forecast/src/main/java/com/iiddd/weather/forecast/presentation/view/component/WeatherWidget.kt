package com.iiddd.weather.forecast.presentation.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
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
    onRefresh: () -> Unit
) {
    val weather = weatherState.value

    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 180.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = weather?.city ?: "Unknown location")
            Spacer(modifier = Modifier.height(8.dp))
            if (weather == null) {
                Text(text = "Forecast coming soon...")
            } else {
                Text(text = "Summary: ${weather.description}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Temp: ${weather.currentTemp}°")
            }
            Spacer(modifier = Modifier.height(12.dp))
            RefreshButton(onRefresh = onRefresh)
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

    WeatherView(
        weatherState = mockWeather,
        onRefresh = {}
    )
}