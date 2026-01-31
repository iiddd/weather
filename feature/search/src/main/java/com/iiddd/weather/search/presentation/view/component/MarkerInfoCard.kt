package com.iiddd.weather.search.presentation.view.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.google.android.gms.maps.model.LatLng
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.search.R as SearchR

@Composable
fun MarkerInfoCard(
    title: String,
    markerLatLng: LatLng,
    onOpenDetails: (latitude: Double?, longitude: Double?) -> Unit,
    onClearMarker: () -> Unit,
    modifier: Modifier = Modifier,
    tailHeight: Dp = WeatherThemeTokens.dimens.spacingLarge,
    tailWidth: Dp = WeatherThemeTokens.dimens.spacingLarge,
) {
    val dimens = WeatherThemeTokens.dimens
    val cardColor = WeatherThemeTokens.colors.surface

    Column(
        modifier = modifier.wrapContentWidth(align = Alignment.CenterHorizontally),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            Card(
                modifier = Modifier.wrapContentWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationLarge),
                shape = WeatherThemeTokens.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = cardColor),
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(
                            start = dimens.spacingMedium,
                            end = dimens.spacingMedium,
                            top = dimens.buttonHeightLarge,
                            bottom = dimens.spacingMedium,
                        ),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            style = WeatherThemeTokens.typography.bodyLarge,
                        )
                    }

                    Spacer(modifier = Modifier.height(height = dimens.spacingSmall))

                    IconButton(
                        onClick = {
                            onOpenDetails(markerLatLng.latitude, markerLatLng.longitude)
                        },
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = stringResource(id = SearchR.string.marker_add_favorite_content_description),
                            tint = WeatherThemeTokens.colors.primary,
                        )
                    }
                }
            }

            IconButton(
                onClick = onClearMarker,
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .padding(all = dimens.spacingSmall)
                    .size(size = dimens.iconSizeLarge),
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(id = SearchR.string.marker_close_content_description),
                    tint = WeatherThemeTokens.colors.onSurface,
                )
            }
        }

        Canvas(
            modifier = Modifier
                .width(width = tailWidth)
                .height(height = tailHeight),
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val path = Path().apply {
                moveTo(x = 0f, y = 0f)
                lineTo(x = canvasWidth, y = 0f)
                lineTo(x = canvasWidth / 2f, y = canvasHeight)
                close()
            }
            drawPath(path = path, color = cardColor, style = Fill)
        }
    }
}

@WeatherPreview
@Composable
private fun MarkerInfoCardPreview() {
    val sampleLatLng = LatLng(52.35, 4.91)
    WeatherTheme {
        MarkerInfoCard(
            title = "Amsterdam, Netherlands",
            markerLatLng = sampleLatLng,
            onOpenDetails = { _, _ -> },
            onClearMarker = {},
        )
    }
}