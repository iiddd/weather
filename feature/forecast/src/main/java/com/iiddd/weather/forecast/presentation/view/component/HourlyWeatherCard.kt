package com.iiddd.weather.forecast.presentation.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iiddd.weather.forecast.domain.model.HourlyForecast
import com.iiddd.weather.forecast.presentation.icons.resolveWeatherIcon

@Composable
fun HourlyWeatherCard(
    forecast: HourlyForecast,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(72.dp)
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon
            val iconRes = resolveWeatherIcon(forecast.icon)
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Temp
            Text(
                text = "${forecast.temp.toInt()}°",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Time
            Text(
                text = forecast.time,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview
@Composable
private fun HourlyWeatherWidgetPreview() {
    val hour = HourlyForecast(
        time = "09:00",
        temp = 13.0,
        icon = "01d"
    )

    HourlyWeatherCard(
        forecast = hour
    )
}