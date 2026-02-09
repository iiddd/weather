package com.iiddd.weather.search.presentation.view.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.iiddd.weather.core.theme.WeatherTheme
import com.iiddd.weather.core.theme.WeatherThemeTokens
import com.iiddd.weather.core.ui.components.PrimaryButton
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.search.R as SearchR

@Composable
fun LocationBottomPanel(
    locationTitle: String,
    isVisible: Boolean,
    onViewDetails: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = WeatherThemeTokens.dimens
    val typography = WeatherThemeTokens.typography
    val colors = WeatherThemeTokens.colors

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier,
    ) {
        Surface(
            color = colors.surface,
            shadowElevation = dimens.elevationLarge,
            shape = RoundedCornerShape(
                topStart = dimens.cornerRadiusLarge,
                topEnd = dimens.cornerRadiusLarge,
            ),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = dimens.spacingExtraLarge),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = locationTitle,
                        style = typography.headlineSmall,
                        color = colors.onSurface,
                    )

                    Spacer(modifier = Modifier.height(height = dimens.spacingLarge))

                    PrimaryButton(
                        onClick = onViewDetails,
                        modifier = Modifier.fillMaxWidth(),
                        buttonText = stringResource(id = SearchR.string.details_button_label)
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(alignment = Alignment.TopEnd)
                        .padding(all = dimens.spacingMedium),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = SearchR.string.marker_close_content_description),
                        tint = colors.onSurfaceVariant,
                        modifier = Modifier.size(size = dimens.iconSizeMedium),
                    )
                }
            }
        }
    }
}

@WeatherPreview
@Composable
private fun LocationBottomPanelPreview() {
    WeatherTheme {
        LocationBottomPanel(
            locationTitle = "Amsterdam, Netherlands",
            isVisible = true,
            onViewDetails = {},
            onDismiss = {},
        )
    }
}
