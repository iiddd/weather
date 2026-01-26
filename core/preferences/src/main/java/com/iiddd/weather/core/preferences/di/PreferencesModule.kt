package com.iiddd.weather.core.preferences.di

import com.iiddd.weather.core.preferences.theme.DataStoreThemeModeRepository
import com.iiddd.weather.core.preferences.theme.providePreferencesDataStore
import com.iiddd.weather.core.ui.theme.ThemeModeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferencesModule = module {
    single { androidContext().providePreferencesDataStore() }
    single<ThemeModeRepository> { DataStoreThemeModeRepository(dataStore = get()) }
}