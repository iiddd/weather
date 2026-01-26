package com.iiddd.weather

import android.app.Application
import com.iiddd.weather.di.ApplicationModule
import com.iiddd.weather.forecast.di.ForecastModule
import com.iiddd.weather.search.di.SearchModule
import com.iiddd.weather.settings.di.SettingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApp)
            modules(
                ApplicationModule.module,
                ForecastModule.module,
                SearchModule.module,
                SettingsModule.module
            )
        }
    }
}