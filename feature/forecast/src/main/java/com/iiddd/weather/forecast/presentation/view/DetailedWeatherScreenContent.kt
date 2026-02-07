package com.iiddd.weather.forecast.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.presentation.previewfixtures.PreviewWeatherProvider
import com.iiddd.weather.forecast.presentation.view.component.DailyForecastWidget
import com.iiddd.weather.forecast.presentation.view.component.HourlyForecastWidget
import com.iiddd.weather.forecast.presentation.view.component.WeatherWidget
import com.iiddd.weather.forecast.R as ForecastR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedWeatherScreenContent(
    weatherState: State<Weather?>,
    isRefreshing: Boolean,
    isFavorite: Boolean,
    onRefresh: () -> Unit,
    onToggleFavorite: () -> Unit,
) {
    val dimens = WeatherThemeTokens.dimens
    val typography = WeatherThemeTokens.typography
    val colors = WeatherThemeTokens.colors

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colors.background,
        contentColor = colors.onBackground,
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
                    .padding(
                        horizontal = dimens.spacingExtraLarge,
                        vertical = dimens.spacingLarge,
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    WeatherWidget(
                        weatherState = weatherState,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    FilledIconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .align(alignment = Alignment.TopEnd)
                            .size(size = dimens.iconButtonSize),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = colors.surfaceVariant,
                            contentColor = if (isFavorite) {
                                colors.primary
                            } else {
                                colors.onSurfaceVariant
                            },
                        ),
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                        )
                    }
                }

                Spacer(modifier = Modifier.height(height = dimens.spacingMedium))

                Text(
                    text = stringResource(id = ForecastR.string.forecast_hourly_header),
                    style = typography.headlineMedium,
                    color = colors.onBackground,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(height = dimens.spacingExtraLarge))

                HourlyForecastWidget(
                    forecasts = weatherState.value?.hourly ?: emptyList(),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(height = dimens.spacingMedium))

                Text(
                    text = stringResource(id = ForecastR.string.forecast_daily_header),
                    style = typography.headlineMedium,
                    color = colors.onBackground,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(height = dimens.spacingExtraLarge))

                DailyForecastWidget(
                    forecasts = weatherState.value?.daily ?: emptyList(),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@WeatherPreview
@Composable
private fun DetailedWeatherScreenContentPreview() {
    val mockState = remember {
        mutableStateOf(PreviewWeatherProvider.sampleWeather)
    }

    WeatherTheme {
        DetailedWeatherScreenContent(
            weatherState = mockState,
            isRefreshing = false,
            isFavorite = false,
            onRefresh = {},
            onToggleFavorite = {},
        )
    }
}