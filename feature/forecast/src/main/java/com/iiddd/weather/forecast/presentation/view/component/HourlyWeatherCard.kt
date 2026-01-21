package com.iiddd.weather.forecast.presentation.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.forecast.domain.model.HourlyForecast
import com.iiddd.weather.forecast.presentation.icons.resolveWeatherIcon

@Composable
fun HourlyWeatherCard(
    forecast: HourlyForecast,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(64.dp)
            .heightIn(min = 96.dp) // ensure enough vertical space so temperature isn't clipped
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = forecast.time,
                style = MaterialTheme.typography.labelSmall
            )

            val iconRes = resolveWeatherIcon(forecast.icon)
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = "${forecast.temp.toInt()}°",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@WeatherPreview
@Composable
private fun HourlyWeatherWidgetPreview() {
    val hour = HourlyForecast(
        time = "09:00",
        temp = 13.0,
        icon = "01d"
    )

    WeatherTheme {
        HourlyWeatherCard(
            forecast = hour
        )
    }
}