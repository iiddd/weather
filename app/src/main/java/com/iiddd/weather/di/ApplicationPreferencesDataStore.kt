package com.iiddd.weather.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val applicationPreferencesDataStoreName: String = "application_preferences"

val Context.applicationPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = applicationPreferencesDataStoreName,
)