package com.iiddd.weather.ui.weather.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.iiddd.weather.ui.weather.view.localcomponents.WeatherView
import com.iiddd.weather.ui.weather.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val weatherState = viewModel.weather.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWeather("Amsterdam")
    }

    WeatherView(
        weatherState = weatherState,
        onRefresh = { viewModel.loadWeather("Amsterdam") } // optional
    )
}