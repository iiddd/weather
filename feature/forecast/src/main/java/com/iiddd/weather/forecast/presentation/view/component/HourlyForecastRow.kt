package com.iiddd.weather.forecast.presentation.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.forecast.domain.model.HourlyForecast

@Composable
fun HourlyForecastRow(
    forecasts: List<HourlyForecast>,
    modifier: Modifier = Modifier,
    cardPadding: Dp = 4.dp,
    horizontalPadding: Dp = 8.dp,
    fadeWidth: Dp = 24.dp,
    spacing: Dp = 2.dp,
    rowHeight: Dp = 128.dp,
    cornerRadius: Dp = 12.dp
) {
    if (forecasts.isEmpty()) return

    val listState = rememberLazyListState()

    Card(
        modifier = modifier
            .wrapContentWidth()
            .height(rowHeight),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box {
            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(rowHeight)
            ) {
                items(
                    items = forecasts,
                    key = { forecast -> "${forecast.time}-${forecast.icon}-${forecast.temp}" }
                ) { item ->
                    HourlyWeatherCard(
                        forecast = item,
                        modifier = Modifier.padding(cardPadding)
                    )
                }
            }

            // Left fade matching card height
            Box(
                modifier = Modifier
                    .width(fadeWidth)
                    .fillMaxHeight()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surface.copy(alpha = 0f)
                            )
                        )
                    )
                    .align(Alignment.CenterStart)
            )

            // Right fade matching card height
            Box(
                modifier = Modifier
                    .width(fadeWidth)
                    .fillMaxHeight()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .align(Alignment.CenterEnd)
            )
        }
    }
}

@WeatherPreview
@Composable
fun HourlyForecastRowPreview() {
    val mockForecasts = listOf(
        HourlyForecast(time = "09:00", temp = 13.0, icon = "01d"),
        HourlyForecast(time = "10:00", temp = 14.0, icon = "02d"),
        HourlyForecast(time = "11:00", temp = 15.0, icon = "03d"),
        HourlyForecast(time = "12:00", temp = 16.0, icon = "04d"),
        HourlyForecast(time = "13:00", temp = 17.0, icon = "09d"),
        HourlyForecast(time = "14:00", temp = 18.0, icon = "10d"),
        HourlyForecast(time = "15:00", temp = 19.0, icon = "11d"),
    )

    WeatherTheme {
        HourlyForecastRow(forecasts = mockForecasts)
    }
}