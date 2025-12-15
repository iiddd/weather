package com.iiddd.weather.forecast.di

import com.iiddd.weather.forecast.BuildConfig
import com.iiddd.weather.forecast.data.api.OpenWeatherApi
import com.iiddd.weather.forecast.data.repository.WeatherRepositoryImpl
import com.iiddd.weather.forecast.domain.repository.WeatherRepository
import com.iiddd.weather.forecast.presentation.viewmodel.ForecastViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object ForecastModule {

    val OPENWEATHER_RETROFIT = named("openweather_retrofit")

    val module = module {
        factory {
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            }
        }

        factory {
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            }
        }

        viewModel { ForecastViewModel(weatherRepository = get()) }

        single(OPENWEATHER_RETROFIT) {
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
                api = get<OpenWeatherApi>()
            )
        }

        factory<OpenWeatherApi> {
            get<Retrofit>(OPENWEATHER_RETROFIT).create(OpenWeatherApi::class.java)
        }
    }
}