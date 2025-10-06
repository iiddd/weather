package com.iiddd.weather.ui.weather.view.localcomponents

import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iiddd.weather.domain.weather.WeatherByCity

@Composable
fun CurrentWeatherView(weather: WeatherByCity?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = weather?.condition ?: "Unknown",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = weather?.degree?.let { "$it°C" } ?: "N/A",
            style = MaterialTheme.typography.titleLarge
        )
    }
}