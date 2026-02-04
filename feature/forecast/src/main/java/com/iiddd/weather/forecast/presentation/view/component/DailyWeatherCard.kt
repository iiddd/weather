package com.iiddd.weather.forecast.presentation.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.forecast.domain.model.DailyForecast
import com.iiddd.weather.forecast.presentation.icons.resolveWeatherIcon
import com.iiddd.weather.forecast.R as ForecastR

@Composable
fun DailyWeatherCard(
    forecast: DailyForecast,
    modifier: Modifier = Modifier,
) {
    val dimens = WeatherThemeTokens.dimens

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = WeatherThemeTokens.colors.surfaceVariant,
        ),
        shape = RoundedCornerShape(size = dimens.cornerRadiusMedium),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationNone),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimens.spacingMedium,
                    vertical = dimens.spacingMedium,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = forecast.day,
                style = WeatherThemeTokens.typography.titleMedium,
                color = WeatherThemeTokens.colors.onSurfaceVariant,
                modifier = Modifier.weight(weight = 1f),
            )

            val iconResource = resolveWeatherIcon(iconCode = forecast.icon)

            Image(
                painter = painterResource(id = iconResource),
                contentDescription = null,
                modifier = Modifier.size(size = dimens.iconSizeLarge),
            )

            Row(
                modifier = Modifier.width(width = dimens.temperatureRowWidth),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(
                        id = ForecastR.string.daily_temperature_day,
                        forecast.tempDay,
                    ),
                    style = WeatherThemeTokens.typography.titleMedium,
                    color = WeatherThemeTokens.colors.onSurfaceVariant,
                )
                Text(
                    text = stringResource(
                        id = ForecastR.string.daily_temperature_night,
                        forecast.tempNight,
                    ),
                    style = WeatherThemeTokens.typography.bodyLarge,
                    color = WeatherThemeTokens.colors.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = dimens.spacingSmall),
                )
            }
        }
    }
}

@WeatherPreview
@Composable
private fun DailyWeatherCardPreview() {
    val dailyForecast = DailyForecast(
        day = "Monday",
        tempDay = 18,
        tempNight = 12,
        icon = "01d",
    )

    WeatherTheme {
        DailyWeatherCard(forecast = dailyForecast)
    }
}
