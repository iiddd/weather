package com.iiddd.weather.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.iiddd.weather.core.preferences.theme.DataStoreThemeModeRepository
import com.iiddd.weather.core.ui.theme.ThemeModeRepository
import com.iiddd.weather.location.data.AndroidCityNameResolver
import com.iiddd.weather.location.data.FusedLocationTracker
import com.iiddd.weather.location.domain.CityNameResolver
import com.iiddd.weather.location.domain.LocationTracker
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object ApplicationModule {

    val module = module {
        // Location
        single<LocationTracker> {
            FusedLocationTracker(
                applicationContext = androidContext(),
            )
        }

        single<CityNameResolver> {
            AndroidCityNameResolver(
                applicationContext = androidContext(),
            )
        }

        // Preferences / DataStore
        single<DataStore<Preferences>> {
            androidContext().applicationPreferencesDataStore
        }

        // Theme
        single<ThemeModeRepository> {
            DataStoreThemeModeRepository(
                dataStore = get(),
            )
        }
    }
}