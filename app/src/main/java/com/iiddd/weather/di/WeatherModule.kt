package com.iiddd.weather.di

import com.iiddd.weather.data.OpenWeatherClient
import com.iiddd.weather.data.WeatherRepository
import com.iiddd.weather.data.retrofit.OpenWeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: OpenWeatherApiService
    ): WeatherRepository = OpenWeatherClient(api)
}