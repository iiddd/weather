package com.iiddd.weather.settings.presentation.view.model

import androidx.annotation.DrawableRes
import com.iiddd.weather.core.ui.theme.ThemeMode

data class ThemeOptionUiModel(
    val themeMode: ThemeMode,
    val title: String,
    @get:DrawableRes val previewDrawableResourceId: Int,
)