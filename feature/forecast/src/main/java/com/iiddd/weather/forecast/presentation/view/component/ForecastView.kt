package com.iiddd.weather.forecast.presentation.view.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.forecast.R as ForecastR
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.presentation.previewfixtures.PreviewWeatherProvider

@Composable
fun ForecastView(weather: Weather?) {
    val dimens = WeatherThemeTokens.dimens

    Card(
        modifier = Modifier
            .wrapContentWidth()
            .padding(all = dimens.spacingLarge),
        colors = CardDefaults.cardColors(
            containerColor = WeatherThemeTokens.colors.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationSmall),
    ) {
        Column(
            modifier = Modifier.padding(all = dimens.spacingLarge),
            horizontalAlignment = Alignment.Start,
        ) {
            if (weather == null) {
                Text(
                    text = stringResource(id = ForecastR.string.forecast_coming_soon),
                    style = WeatherThemeTokens.typography.bodyMedium,
                    color = WeatherThemeTokens.colors.onSurfaceVariant,
                )
            } else {
                Text(
                    text = stringResource(id = ForecastR.string.weather_summary, weather.description),
                    style = WeatherThemeTokens.typography.bodyMedium,
                    color = WeatherThemeTokens.colors.onSurface,
                )
                Spacer(modifier = Modifier.height(height = dimens.spacingMedium))
            }
        }
    }
}

@WeatherPreview
@Composable
private fun ForecastViewPreviewEmpty() {
    WeatherTheme {
        ForecastView(weather = null)
    }
}

@WeatherPreview
@Composable
private fun ForecastViewPreviewWithData() {
    WeatherTheme {
        ForecastView(weather = PreviewWeatherProvider.sampleWeather)
    }
}