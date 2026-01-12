package com.iiddd.weather.search.di

import com.iiddd.weather.core.utils.coroutines.DefaultDispatcherProvider
import com.iiddd.weather.core.utils.coroutines.DispatcherProvider
import com.iiddd.weather.search.BuildConfig
import com.iiddd.weather.search.R
import com.iiddd.weather.search.data.SearchRepositoryImpl
import com.iiddd.weather.search.data.api.GeocodingApi
import com.iiddd.weather.search.domain.SearchRepository
import com.iiddd.weather.search.presentation.viewmodel.SearchViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object SearchModule {

    val GOOGLE_MAPS_RETROFIT = named("google_maps_retrofit")

    val module = module {
        single<DispatcherProvider> {
            DefaultDispatcherProvider()
        }

        factory {
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            }
        }

        factory {
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }
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

        single(GOOGLE_MAPS_RETROFIT) {
            val json = get<Json>()
            val contentType = "application/json".toMediaType()

            Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .client(get())
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()
        }

        factory<GeocodingApi> {
            get<Retrofit>(GOOGLE_MAPS_RETROFIT).create(GeocodingApi::class.java)
        }

        single {
            androidContext().getString(R.string.google_maps_key)
        }

        single<SearchRepository> {
            SearchRepositoryImpl(
                geocodingApi = get<GeocodingApi>(),
                apiKey = get<String>(),
                dispatcherProvider = get<DispatcherProvider>()
            )
        }

        viewModel {
            SearchViewModel(
                repository = get<SearchRepository>(),
                dispatcherProvider = get<DispatcherProvider>()
            )
        }
    }
}