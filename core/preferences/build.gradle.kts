plugins {
    id("com.iiddd.weather.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.iiddd.weather.core.preferences"
}

dependencies {
    implementation(projects.core.theme)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.android)
}