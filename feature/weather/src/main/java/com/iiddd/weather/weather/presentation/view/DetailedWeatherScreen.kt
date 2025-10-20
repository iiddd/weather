package com.iiddd.weather.weather.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.iiddd.weather.weather.presentation.view.localcomponents.WeatherView
import com.iiddd.weather.weather.presentation.viewmodel.DetailedScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailedWeatherScreen(viewModel: DetailedScreenViewModel = koinViewModel()) {
    val weatherState = viewModel.weather.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWeather(52.35, 4.91) // Coordinates for Amsterdam
    }

    WeatherView(
        weatherState = weatherState,
        onRefresh = { viewModel.loadWeather(52.35, 4.91) }
    )
}