plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.iiddd.weather.home"
    compileSdk = 36

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions { kotlinCompilerExtensionVersion = libs.versions.kotlin.get() }

    defaultConfig { minSdk = 31 }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(platform(libs.compose.bom))

    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    testImplementation(libs.junit)
}