plugins {
    id("com.iiddd.weather.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android { namespace = "com.iiddd.weather.core.location" }

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.play.services.location)
    implementation(libs.koin.android)

    // Networking for Google Geocoding API
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
}