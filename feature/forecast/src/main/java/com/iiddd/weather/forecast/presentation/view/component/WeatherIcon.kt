package com.iiddd.weather.forecast.presentation.view.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.iiddd.weather.core.ui.theme.LocalThemeMode
import com.iiddd.weather.core.ui.theme.ThemeMode
import com.iiddd.weather.forecast.presentation.icons.resolveWeatherIcon

@Composable
fun WeatherIcon(
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
