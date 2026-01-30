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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.presentation.previewfixtures.PreviewWeatherProvider
import com.iiddd.weather.forecast.presentation.view.component.HourlyForecastRow
import com.iiddd.weather.forecast.presentation.view.component.WeatherView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedWeatherScreenContent(
    weatherState: State<Weather?>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
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
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                WeatherView(
                    weatherState = weatherState,
                    onRefresh = onRefresh,
                )

                Spacer(modifier = Modifier.height(12.dp))

                HourlyForecastRow(
                    forecasts = weatherState.value?.hourly ?: emptyList(),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@WeatherPreview
@Composable
fun DetailedWeatherScreenContentPreview() {
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