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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.forecast.domain.model.DailyForecast
import com.iiddd.weather.forecast.domain.model.HourlyForecast
import com.iiddd.weather.forecast.domain.model.Weather

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

@Preview(showBackground = true)
@Composable
fun ForecastViewPreview_Empty() {
    WeatherTheme {
        ForecastView(weather = null)
    }
}

@Preview(showBackground = true)
@Composable
fun ForecastViewPreview_WithData() {
    val mock = Weather(
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
    WeatherTheme {
        ForecastView(weather = mock)
    }
}