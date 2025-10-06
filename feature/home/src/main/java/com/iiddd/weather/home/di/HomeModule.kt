package com.iiddd.weather.home.di

import com.iiddd.weather.home.BuildConfig
import com.iiddd.weather.home.data.api.OpenWeatherApiService
import com.iiddd.weather.home.data.repository.WeatherRepositoryImpl
import com.iiddd.weather.home.domain.repository.WeatherRepository
import com.iiddd.weather.home.presentation.viewmodel.HomeViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val homeModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }
    }

    viewModel { HomeViewModel(weatherRepository = get()) }

    single {
        val json = get<Json>()
        val contentType = "application/json".toMediaType()

        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(get())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .callTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    single<WeatherRepository> {
        WeatherRepositoryImpl(
            api = get<OpenWeatherApiService>()
        )
    }

    single<OpenWeatherApiService> { get<Retrofit>().create(OpenWeatherApiService::class.java) }
}