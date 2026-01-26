package com.iiddd.weather.core.preferences.theme

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore

private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "weather_preferences"
)

fun Context.providePreferencesDataStore(): DataStore<Preferences> =
    preferencesDataStore