package com.iiddd.weather.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.iiddd.weather.BuildConfig
import com.iiddd.weather.core.preferences.theme.DataStoreThemeModeRepository
import com.iiddd.weather.core.ui.theme.ThemeModeRepository
import com.iiddd.weather.location.data.FusedLocationTracker
import com.iiddd.weather.location.data.GoogleGeocodingService
import com.iiddd.weather.location.data.api.GeocodingApi
import com.iiddd.weather.location.domain.GeocodingService
import com.iiddd.weather.location.domain.LocationTracker
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object ApplicationModule {

    private val GEOCODING_RETROFIT = named("geocoding_retrofit")

    val module = module {
        // Location
        single<LocationTracker> {
            FusedLocationTracker(
                applicationContext = androidContext(),
            )
        }

        // Geocoding
        single {
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            }
        }

        single {
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

        single(qualifier = GEOCODING_RETROFIT) {
            val json = get<Json>()
            val contentType = "application/json".toMediaType()

            Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .client(get())
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()
        }

        factory<GeocodingApi> {
            get<Retrofit>(qualifier = GEOCODING_RETROFIT).create(GeocodingApi::class.java)
        }

        single<GeocodingService> {
            GoogleGeocodingService(
                geocodingApi = get(),
                apiKey = BuildConfig.GOOGLE_MAPS_API_KEY,
            )
        }

        // Preferences / DataStore
        single<DataStore<Preferences>> {
            androidContext().applicationPreferencesDataStore
        }

        // Theme
        single<ThemeModeRepository> {
            DataStoreThemeModeRepository(
                dataStore = get(),
            )
        }
    }
}