package com.iiddd.weather.core.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iiddd.weather.core.theme.WeatherTheme
import com.iiddd.weather.core.theme.WeatherThemeTokens.dimens

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(size = dimens.cornerRadiusMedium),
    ) {
        Text(text = buttonText)
    }
}

@WeatherPreview
@Composable
private fun PrimaryButtonPreview() {
    WeatherTheme {
        PrimaryButton(
            onClick = {},
            buttonText = "Primary Button",
        )
    }
}