package com.iiddd.weather.search.presentation.view.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.theme.WeatherTheme
import com.iiddd.weather.core.theme.WeatherThemeTokens
import com.iiddd.weather.search.R as SearchR

@Composable
internal fun MyLocationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = modifier.size(size = 48.dp),
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = WeatherThemeTokens.colors.surface,
            contentColor = WeatherThemeTokens.colors.onSurface,
        ),
    ) {
        Icon(
            imageVector = Icons.Default.MyLocation,
            contentDescription = stringResource(id = SearchR.string.my_location_button_description),
        )
    }
}

@WeatherPreview
@Composable
private fun MyLocationButtonPreview() {
    WeatherTheme {
        MyLocationButton(onClick = {})
    }
}
