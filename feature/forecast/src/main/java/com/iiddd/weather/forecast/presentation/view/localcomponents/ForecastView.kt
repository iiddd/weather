package com.iiddd.weather.forecast.presentation.view.localcomponents

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iiddd.weather.forecast.domain.model.Weather

@Composable
fun ForecastView(weather: Weather?) {
    // Future forecast display
    Text(text = "Forecast coming soon...")
}