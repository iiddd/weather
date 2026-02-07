package com.iiddd.weather.forecast.presentation.view.component

import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.forecast.domain.model.HourlyForecast
import com.iiddd.weather.forecast.R as ForecastR

@Composable
fun HourlyForecastWidget(
    forecasts: List<HourlyForecast>,
    modifier: Modifier = Modifier,
) {
    if (forecasts.isEmpty()) return

    val dimens = WeatherThemeTokens.dimens
    val backgroundColor = WeatherThemeTokens.colors.background
    val listState = rememberLazyListState()

    val canScrollBackward by remember {
        derivedStateOf { listState.canScrollBackward }
    }

    val canScrollForward by remember {
        derivedStateOf { listState.canScrollForward }
    }

    Card(
        modifier = modifier.height(height = dimens.cardHeightMedium),
        shape = RoundedCornerShape(size = dimens.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationNone),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            val nowLabel = stringResource(id = ForecastR.string.forecast_hourly_now)
            LazyRow(
                state = listState,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = dimens.spacingSmall),
                modifier = Modifier.fillMaxSize(),
            ) {
                itemsIndexed(
                    items = forecasts,
                    key = { index, forecast -> "${index}-${forecast.time}-${forecast.icon}-${forecast.temp}" },
                ) { index, hourlyForecast ->
                    val displayTime = if (index == 0) nowLabel else hourlyForecast.time
                    HourlyWeatherCard(
                        forecast = hourlyForecast,
                        displayTime = displayTime,
                    )
                }
            }

            // Left fade - only show when there are items to scroll back to
            androidx.compose.animation.AnimatedVisibility(
                visible = canScrollBackward,
                enter = expandHorizontally(),
                exit = shrinkHorizontally(),
                modifier = Modifier.align(alignment = Alignment.CenterStart),
            ) {
                Box(
                    modifier = Modifier
                        .width(width = dimens.fadeWidth)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    backgroundColor,
                                    backgroundColor.copy(alpha = 0f),
                                ),
                            ),
                        ),
                )
            }

            // Right fade - only show when there are items to scroll to
            androidx.compose.animation.AnimatedVisibility(
                visible = canScrollForward,
                enter = expandHorizontally(),
                exit = shrinkHorizontally(),
                modifier = Modifier.align(alignment = Alignment.CenterEnd),
            ) {
                Box(
                    modifier = Modifier
                        .width(width = dimens.fadeWidth)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    backgroundColor.copy(alpha = 0f),
                                    backgroundColor,
                                ),
                            ),
                        ),
                )
            }
        }
    }
}


@WeatherPreview
@Composable
private fun HourlyForecastWidgetPreview() {
    val mockForecasts = listOf(
        HourlyForecast(time = "09:00", temp = 13, icon = "01d"),
        HourlyForecast(time = "10:00", temp = 14, icon = "02d"),
        HourlyForecast(time = "11:00", temp = 15, icon = "03d"),
        HourlyForecast(time = "12:00", temp = 16, icon = "04d"),
        HourlyForecast(time = "13:00", temp = 17, icon = "09d"),
        HourlyForecast(time = "14:00", temp = 18, icon = "10d"),
        HourlyForecast(time = "15:00", temp = 19, icon = "11d"),
    )

    WeatherTheme {
        HourlyForecastWidget(forecasts = mockForecasts)
    }
}