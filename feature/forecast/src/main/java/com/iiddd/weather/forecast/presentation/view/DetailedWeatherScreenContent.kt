package com.iiddd.weather.forecast.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.presentation.previewfixtures.PreviewWeatherProvider
import com.iiddd.weather.forecast.presentation.view.component.DailyForecastWidget
import com.iiddd.weather.forecast.presentation.view.component.HourlyForecastWidget
import com.iiddd.weather.forecast.presentation.view.component.WeatherWidget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedWeatherScreenContent(
    weatherState: State<Weather?>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    val dimens = WeatherThemeTokens.dimens

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = WeatherThemeTokens.colors.background,
        contentColor = WeatherThemeTokens.colors.onBackground,
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
                    .padding(horizontal = dimens.spacingExtraLarge),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                WeatherWidget(
                    weatherState = weatherState,
                    onRefresh = onRefresh,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(height = dimens.spacingLarge))

                HourlyForecastWidget(
                    forecasts = weatherState.value?.hourly ?: emptyList(),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(height = dimens.spacingLarge))

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
            onRefresh = {},
        )
    }
}