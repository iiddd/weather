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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.forecast.domain.model.HourlyForecast

@Composable
fun HourlyForecastRow(
    forecasts: List<HourlyForecast>,
    modifier: Modifier = Modifier,
) {
    if (forecasts.isEmpty()) return

    val dimens = WeatherThemeTokens.dimens
    val surfaceColor = WeatherThemeTokens.colors.surface
    val listState = rememberLazyListState()

    Card(
        modifier = modifier
            .wrapContentWidth()
            .height(height = dimens.cardHeightMedium),
        shape = RoundedCornerShape(size = dimens.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationSmall),
    ) {
        Box {
            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = dimens.spacingMedium),
                horizontalArrangement = Arrangement.spacedBy(space = dimens.spacingExtraSmall),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(height = dimens.cardHeightMedium),
            ) {
                items(
                    items = forecasts,
                    key = { forecast -> "${forecast.time}-${forecast.icon}-${forecast.temp}" },
                ) { item ->
                    HourlyWeatherCard(
                        forecast = item,
                        modifier = Modifier.padding(all = dimens.spacingSmall),
                    )
                }
            }

            // Left fade matching card height
            Box(
                modifier = Modifier
                    .width(width = dimens.fadeWidth)
                    .fillMaxHeight()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                surfaceColor,
                                surfaceColor.copy(alpha = 0f),
                            ),
                        ),
                    )
                    .align(alignment = Alignment.CenterStart),
            )

            // Right fade matching card height
            Box(
                modifier = Modifier
                    .width(width = dimens.fadeWidth)
                    .fillMaxHeight()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                surfaceColor.copy(alpha = 0f),
                                surfaceColor,
                            ),
                        ),
                    )
                    .align(alignment = Alignment.CenterEnd),
            )
        }
    }
}

@WeatherPreview
@Composable
private fun HourlyForecastRowPreview() {
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
        HourlyForecastRow(forecasts = mockForecasts)
    }
}