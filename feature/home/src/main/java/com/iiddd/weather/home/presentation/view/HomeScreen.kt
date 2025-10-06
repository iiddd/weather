package com.iiddd.weather.home.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.iiddd.weather.home.presentation.view.localcomponents.WeatherView
import com.iiddd.weather.home.presentation.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val weatherState = viewModel.weather.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWeather("Amsterdam")
    }

    WeatherView(
        weatherState = weatherState,
        onRefresh = { viewModel.loadWeather("Amsterdam") }
    )
}