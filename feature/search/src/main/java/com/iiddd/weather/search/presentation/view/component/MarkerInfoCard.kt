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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.iiddd.weather.core.ui.theme.WeatherTheme

@Composable
fun MarkerInfoCard(
    title: String,
    markerLatLng: LatLng,
    onOpenDetails: (lat: Double?, lon: Double?) -> Unit,
    onClearMarker: () -> Unit,
    modifier: Modifier = Modifier,
    tailHeight: Dp = 16.dp,
    tailWidth: Dp = 16.dp
) {
    val cardColor = MaterialTheme.colorScheme.surface

    Column(
        modifier = modifier.wrapContentWidth(align = Alignment.CenterHorizontally),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Card(
                modifier = Modifier.wrapContentWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = cardColor)
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 12.dp, end = 12.dp, top = 44.dp, bottom = 12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = title)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    IconButton(
                        onClick = {
                            onOpenDetails(markerLatLng.latitude, markerLatLng.longitude)
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Add favorite")
                    }
                }
            }

            IconButton(
                onClick = onClearMarker,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp)
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }
        }

        Canvas(
            modifier = Modifier
                .width(tailWidth)
                .height(tailHeight)
        ) {
            val w = size.width
            val h = size.height
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(w, 0f)
                lineTo(w / 2f, h)
                close()
            }
            drawPath(path = path, color = cardColor, style = Fill)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MarkerInfoCardPreview() {
    val sample = LatLng(52.35, 4.91)
    WeatherTheme {
        MarkerInfoCard(
            title = "Amsterdam, Netherlands",
            markerLatLng = sample,
            onOpenDetails = { _, _ -> },
            onClearMarker = {},
        )
    }
}