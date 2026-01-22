package com.iiddd.weather.forecast.presentation.view.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.presentation.previewfixtures.PreviewWeatherProvider

@Composable
fun ForecastView(
    weather: Weather?
) {
    Card(
        modifier = Modifier
            .wrapContentWidth()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            if (weather == null) {
                Text(text = "Forecast coming soon...")
            } else {
                Text(text = "Summary: ${weather.description}")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@WeatherPreview
@Composable
fun ForecastViewPreviewEmpty() {
    WeatherTheme {
        ForecastView(weather = null)
    }
}

@WeatherPreview
@Composable
fun ForecastViewPreviewWithData() {
    WeatherTheme {
        ForecastView(weather = PreviewWeatherProvider.sampleWeather)
    }
}