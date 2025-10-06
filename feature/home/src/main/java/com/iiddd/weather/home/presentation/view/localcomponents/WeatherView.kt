package com.iiddd.weather.home.presentation.view.localcomponents

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iiddd.weather.home.domain.model.WeatherByCity

@Composable
fun WeatherView(
    weatherState: State<WeatherByCity?>,
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
            CurrentWeatherView(weatherState.value)
            ForecastView(weatherState.value)
        }
    }
}

@Preview
@Composable
fun WeatherViewPreview() {
    val mockWeather = object : State<WeatherByCity?> {
        override val value: WeatherByCity? = WeatherByCity(
            degree = "13",
            condition = "Clear"
        )
    }

    WeatherView(weatherState = mockWeather)
}