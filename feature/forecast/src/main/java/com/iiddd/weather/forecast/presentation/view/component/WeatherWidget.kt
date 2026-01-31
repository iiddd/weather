package com.iiddd.weather.forecast.presentation.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.presentation.previewfixtures.PreviewWeatherProvider
import com.iiddd.weather.forecast.R as ForecastR

@Composable
fun WeatherView(
    weatherState: State<Weather?>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val weather = weatherState.value
    val dimens = WeatherThemeTokens.dimens

    Card(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = dimens.cardHeightLarge),
        colors = CardDefaults.cardColors(
            containerColor = WeatherThemeTokens.colors.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationSmall),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimens.spacingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = weather?.city ?: stringResource(id = ForecastR.string.unknown_location),
                style = WeatherThemeTokens.typography.titleMedium,
                color = WeatherThemeTokens.colors.onSurface,
            )
            Spacer(modifier = Modifier.height(height = dimens.spacingMedium))
            if (weather == null) {
                Text(
                    text = stringResource(id = ForecastR.string.forecast_coming_soon),
                    style = WeatherThemeTokens.typography.bodyMedium,
                    color = WeatherThemeTokens.colors.onSurfaceVariant,
                )
            } else {
                Text(
                    text = stringResource(
                        id = ForecastR.string.weather_summary,
                        weather.description,
                    ),
                    style = WeatherThemeTokens.typography.bodyMedium,
                    color = WeatherThemeTokens.colors.onSurface,
                )
                Spacer(modifier = Modifier.height(height = dimens.spacingMedium))
                Text(
                    text = stringResource(
                        id = ForecastR.string.weather_temperature,
                        weather.currentTemp,
                    ),
                    style = WeatherThemeTokens.typography.headlineLarge,
                    color = WeatherThemeTokens.colors.onSurface,
                )
            }
            Spacer(modifier = Modifier.height(height = dimens.spacingLarge))
            RefreshButton(onRefresh = onRefresh)
        }
    }
}

@WeatherPreview
@Composable
private fun WeatherViewPreview() {
    val mockWeather: State<Weather?> = remember {
        mutableStateOf(PreviewWeatherProvider.sampleWeather)
    }

    WeatherTheme {
        WeatherView(
            weatherState = mockWeather,
            onRefresh = {},
        )
    }
}