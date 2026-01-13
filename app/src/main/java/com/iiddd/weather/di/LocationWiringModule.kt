package com.iiddd.weather.di

import com.iiddd.weather.location.data.FusedLocationTracker
import com.iiddd.weather.location.domain.LocationTracker
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val locationWiringModule = module {
    single<LocationTracker> {
        FusedLocationTracker(
            context = androidContext(),
        )
    }
}