package com.iiddd.weather.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.iiddd.weather.core.theme.WeatherTheme
import com.iiddd.weather.core.theme.WeatherThemeTokens
import com.iiddd.weather.core.ui.R

@Composable
fun ErrorScreen(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = WeatherThemeTokens.dimens
    val colors = WeatherThemeTokens.colors
    val typography = WeatherThemeTokens.typography

    Surface(
        modifier = modifier.fillMaxSize(),
        color = colors.background,
        contentColor = colors.onBackground,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(space = dimens.spacingMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = errorMessage,
                    style = typography.bodyLarge,
                    color = colors.error,
                )

                PrimaryButton(
                    buttonText = stringResource(R.string.error_screen_retry_button),
                    onClick = onRetry,
                )
            }
        }
    }
}

@WeatherPreview
@Composable
private fun ErrorScreenPreview() {
    WeatherTheme {
        ErrorScreen(
            errorMessage = "An unexpected error occurred.",
            onRetry = {}
        )
    }
}