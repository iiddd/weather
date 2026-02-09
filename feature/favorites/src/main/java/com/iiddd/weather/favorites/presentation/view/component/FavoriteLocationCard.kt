package com.iiddd.weather.favorites.presentation.view.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.theme.LocalThemeMode
import com.iiddd.weather.core.theme.ThemeMode
import com.iiddd.weather.core.theme.WeatherTheme
import com.iiddd.weather.core.theme.WeatherThemeTokens
import com.iiddd.weather.core.preferences.favorites.FavoriteLocation
import com.iiddd.weather.favorites.R
import com.iiddd.weather.favorites.presentation.model.FavoriteLocationWithWeather
import com.iiddd.weather.forecast.presentation.icons.resolveWeatherIcon

@Composable
fun FavoriteLocationCard(
    favoriteLocationWithWeather: FavoriteLocationWithWeather,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = WeatherThemeTokens.dimens

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = WeatherThemeTokens.colors.surfaceVariant,
        ),
        shape = RoundedCornerShape(size = dimens.cornerRadiusMedium),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationNone),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimens.spacingMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            favoriteLocationWithWeather.weatherIcon?.let { iconCode ->
                FavoriteWeatherIcon(
                    iconCode = iconCode,
                    modifier = Modifier.size(size = dimens.iconSizeLarge),
                )
                Spacer(modifier = Modifier.width(width = dimens.spacingMedium))
            }

            Text(
                text = favoriteLocationWithWeather.favoriteLocation.cityName,
                style = WeatherThemeTokens.typography.titleMedium,
                color = WeatherThemeTokens.colors.onSurfaceVariant,
                modifier = Modifier.weight(weight = 1f),
            )

            favoriteLocationWithWeather.currentTemperature?.let { temperature ->
                Text(
                    text = stringResource(id = R.string.favorites_temperature, temperature),
                    style = WeatherThemeTokens.typography.headlineSmall,
                    color = WeatherThemeTokens.colors.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun FavoriteWeatherIcon(
    iconCode: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    val context = LocalContext.current
    val currentConfiguration = LocalConfiguration.current
    val themeMode = LocalThemeMode.current
    val isSystemDark = isSystemInDarkTheme()

    val isDarkTheme = when (themeMode) {
        ThemeMode.System -> isSystemDark
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }

    val iconResource = resolveWeatherIcon(iconCode = iconCode)

    val painter = remember(iconResource, isDarkTheme) {
        val configuration = Configuration(currentConfiguration).apply {
            uiMode = if (isDarkTheme) {
                (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or Configuration.UI_MODE_NIGHT_YES
            } else {
                (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or Configuration.UI_MODE_NIGHT_NO
            }
        }
        val themedContext = context.createConfigurationContext(configuration)
        val drawable = ResourcesCompat.getDrawable(
            themedContext.resources,
            iconResource,
            themedContext.theme,
        )
        BitmapPainter(drawable!!.toBitmap().asImageBitmap())
    }

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
    )
}

@WeatherPreview
@Composable
private fun FavoriteLocationCardPreview() {
    WeatherTheme {
        FavoriteLocationCard(
            favoriteLocationWithWeather = FavoriteLocationWithWeather(
                favoriteLocation = FavoriteLocation(
                    id = "1",
                    cityName = "Tokyo, Japan",
                    latitude = 35.6762,
                    longitude = 139.6503,
                ),
                currentTemperature = 22,
                weatherDescription = "Clear",
                weatherIcon = "01d",
            ),
            onClick = {},
        )
    }
}
