plugins {
    id("com.iiddd.weather.android.library")
    id("com.iiddd.weather.android.compose")
    id("com.iiddd.weather.android.junit")
}

android {
    namespace = "com.iiddd.weather.settings"
}

dependencies {
    // Modules
    implementation(projects.core.ui)
    implementation(projects.core.utils)

    implementation(libs.androidx.core.ktx)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Testing
    testImplementation(projects.core.testUtils)
}