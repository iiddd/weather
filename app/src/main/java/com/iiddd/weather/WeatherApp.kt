package com.iiddd.weather

import android.app.Application
import com.iiddd.weather.di.locationWiringModule
import com.iiddd.weather.forecast.di.ForecastModule
import com.iiddd.weather.search.di.SearchModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApp)
            modules(
                locationWiringModule,
                ForecastModule.module,
                SearchModule.module,
            )
        }
    }
}