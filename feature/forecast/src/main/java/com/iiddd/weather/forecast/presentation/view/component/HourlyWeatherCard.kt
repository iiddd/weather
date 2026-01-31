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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.forecast.domain.model.HourlyForecast
import com.iiddd.weather.forecast.presentation.icons.resolveWeatherIcon

@Composable
fun HourlyWeatherCard(
    forecast: HourlyForecast,
    modifier: Modifier = Modifier,
) {
    val dimens = WeatherThemeTokens.dimens

    Card(
        modifier = modifier
            .width(width = dimens.cardWidthSmall)
            .heightIn(min = dimens.cardHeightSmall)
            .padding(all = dimens.spacingSmall),
        colors = CardDefaults.cardColors(
            containerColor = WeatherThemeTokens.colors.surfaceVariant,
        ),
        shape = RoundedCornerShape(size = dimens.cornerRadiusMedium),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationSmall),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimens.cornerRadiusMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = dimens.spacingMedium),
        ) {
            Text(
                text = forecast.time,
                style = WeatherThemeTokens.typography.labelSmall,
                color = WeatherThemeTokens.colors.onSurfaceVariant,
            )

            val iconRes = resolveWeatherIcon(iconCode = forecast.icon)
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(size = dimens.iconSizeLarge),
            )

            Text(
                text = "${forecast.temp}°",
                style = WeatherThemeTokens.typography.bodyLarge,
                color = WeatherThemeTokens.colors.onSurfaceVariant,
            )
        }
    }
}

@WeatherPreview
@Composable
private fun HourlyWeatherWidgetPreview() {
    val hour = HourlyForecast(
        time = "09:00",
        temp = 13,
        icon = "01d",
    )

    WeatherTheme {
        HourlyWeatherCard(forecast = hour)
    }
}