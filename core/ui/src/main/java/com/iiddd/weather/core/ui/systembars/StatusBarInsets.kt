package com.iiddd.weather.core.ui.systembars

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable

object WeatherWindowInsets {

    val None: WindowInsets
        @Composable
        get() = WindowInsets(left = 0, top = 0, right = 0, bottom = 0)

    val Content: WindowInsets
        @Composable
        get() = WindowInsets.systemBars

    @OptIn(ExperimentalLayoutApi::class)
    val ContentNoBottomBar: WindowInsets
        @Composable
        get() = WindowInsets.systemBars.only(WindowInsetsSides.Top)
}