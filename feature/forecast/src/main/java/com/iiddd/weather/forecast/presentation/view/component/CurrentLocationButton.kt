package com.iiddd.weather.forecast.presentation.view.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.forecast.R as ForecastR

@Composable
fun CurrentLocationButton(
    onCurrentLocationRequested: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = WeatherThemeTokens.dimens

    FilledIconButton(
        onClick = onCurrentLocationRequested,
        shape = RoundedCornerShape(dimens.cornerRadiusSmall),
        modifier = modifier.size(size = dimens.buttonHeightLarge),
    ) {
        Icon(
            imageVector = Icons.Filled.MyLocation,
            contentDescription = stringResource(id = ForecastR.string.current_location_button_label),
            modifier = Modifier.size(size = dimens.iconSizeMedium),
        )
    }
}

@WeatherPreview
@Composable
private fun CurrentLocationButtonPreview() {
    WeatherTheme {
        CurrentLocationButton(onCurrentLocationRequested = {})
    }
}
