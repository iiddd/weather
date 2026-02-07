package com.iiddd.weather.search.presentation.view.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.search.R as SearchR

@Composable
internal fun DetailsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = WeatherThemeTokens.dimens

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(size = dimens.cornerRadiusMedium)
    ) {
        Text(
            text = stringResource(SearchR.string.details_button_label),
        )
    }
}

@WeatherPreview
@Composable
private fun DetailsButtonPreview() {
    WeatherTheme {
        DetailsButton(
            onClick = {}
        )
    }
}