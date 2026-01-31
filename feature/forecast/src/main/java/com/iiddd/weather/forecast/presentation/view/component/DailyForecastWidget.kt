package com.iiddd.weather.forecast.presentation.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.forecast.domain.model.DailyForecast

@Composable
fun DailyForecastWidget(
    forecasts: List<DailyForecast>,
    modifier: Modifier = Modifier,
) {
    if (forecasts.isEmpty()) return

    val dimens = WeatherThemeTokens.dimens
    val backgroundColor = WeatherThemeTokens.colors.background

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = dimens.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationNone),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimens.spacingMedium),
            verticalArrangement = Arrangement.spacedBy(space = dimens.spacingSmall),
        ) {
            forecasts.forEach { dailyForecast ->
                DailyWeatherCard(forecast = dailyForecast)
            }
        }
    }
}

@WeatherPreview
@Composable
private fun DailyForecastWidgetPreview() {
    val mockForecasts = listOf(
        DailyForecast(day = "Monday", tempDay = 18, tempNight = 12, icon = "01d"),
        DailyForecast(day = "Tuesday", tempDay = 20, tempNight = 14, icon = "02d"),
        DailyForecast(day = "Wednesday", tempDay = 17, tempNight = 11, icon = "03d"),
        DailyForecast(day = "Thursday", tempDay = 15, tempNight = 9, icon = "04d"),
        DailyForecast(day = "Friday", tempDay = 19, tempNight = 13, icon = "09d"),
        DailyForecast(day = "Saturday", tempDay = 21, tempNight = 15, icon = "10d"),
        DailyForecast(day = "Sunday", tempDay = 22, tempNight = 16, icon = "11d"),
    )

    WeatherTheme {
        DailyForecastWidget(forecasts = mockForecasts)
    }
}
