package com.iiddd.weather.settings.di

import com.iiddd.weather.settings.presentation.viewmodel.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

object SettingsModule {

    val module = module {

        viewModel {
            SettingsViewModel(
                themeModeRepository = get(),
            )
        }
    }
}