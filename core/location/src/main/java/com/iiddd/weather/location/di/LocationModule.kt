package com.iiddd.weather.location.di

import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext
import com.iiddd.weather.location.domain.LocationTracker
import com.iiddd.weather.location.data.FusedLocationTracker

val locationModule = module {
    single<LocationTracker> { FusedLocationTracker(androidContext()) }
}