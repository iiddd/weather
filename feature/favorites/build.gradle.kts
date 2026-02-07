plugins {
    id("com.iiddd.weather.android.library")
    id("com.iiddd.weather.android.compose")
    id("com.iiddd.weather.android.junit")

    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.iiddd.weather.favorites"
}

dependencies {
    // Modules
    implementation(projects.core.ui)
    implementation(projects.core.network)
    implementation(projects.core.utils)
    implementation(projects.core.preferences)
    implementation(projects.feature.forecast)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.androidx.core.ktx)

    // UI extras
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.ui.graphics)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // KotlinX Serialization
    implementation(libs.kotlinx.serialization.json)

    // DataStore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.android)

    // Testing
    testImplementation(projects.core.testUtils)
    testImplementation(libs.mockito.kotlin)
}

