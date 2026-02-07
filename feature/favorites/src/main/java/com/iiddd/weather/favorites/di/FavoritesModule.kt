package com.iiddd.weather.favorites.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.iiddd.weather.core.preferences.favorites.DataStoreFavoritesRepository
import com.iiddd.weather.core.preferences.favorites.FavoritesRepository
import com.iiddd.weather.favorites.presentation.viewmodel.FavoritesViewModel
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

object FavoritesModule {

    private val FAVORITES_DATASTORE = named("favorites_datastore")

    val module = module {

        single(qualifier = FAVORITES_DATASTORE) {
            PreferenceDataStoreFactory.create {
                File(androidContext().filesDir, "favorites_preferences.preferences_pb")
            }
        }

        single {
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            }
        }

        single<FavoritesRepository> {
            DataStoreFavoritesRepository(
                dataStore = get(qualifier = FAVORITES_DATASTORE),
                json = get(),
            )
        }

        viewModel {
            FavoritesViewModel(
                favoritesRepository = get(),
                weatherRepository = get(),
                dispatcherProvider = get(),
            )
        }
    }
}
