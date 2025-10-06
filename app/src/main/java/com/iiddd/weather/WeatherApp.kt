package com.iiddd.weather

import android.app.Application
import com.iiddd.weather.home.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApp)
            modules(
                homeModule,
            )
        }
    }
}