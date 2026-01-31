package com.iiddd.weather.forecast.presentation.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.forecast.R as ForecastR

@Composable
fun RefreshButton(onRefresh: () -> Unit) {
    val dimens = WeatherThemeTokens.dimens

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = onRefresh,
            shape = RoundedCornerShape(size = dimens.cornerRadiusMedium),
        ) {
            Text(
                text = stringResource(id = ForecastR.string.refresh_button_label),
                style = WeatherThemeTokens.typography.labelLarge,
            )
        }
    }
}

@WeatherPreview
@Composable
private fun RefreshButtonPreview() {
    WeatherTheme {
        RefreshButton(onRefresh = {})
    }
}