import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.iiddd.weather.search"
    compileSdk = 36

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    defaultConfig {
        minSdk = 31
        val props = Properties().apply {
            val f = rootProject.file("apikeys.properties")
            if (f.exists()) f.inputStream().use(::load)
        }
        val mapsKey = props.getProperty("GOOGLE_MAPS_API_KEY") ?: ""
        resValue("string", "google_maps_key", mapsKey)
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(project(":core:ui"))

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(platform(libs.compose.bom))

    // Google Maps
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

    debugImplementation(libs.compose.ui.tooling)

    testImplementation(libs.junit)
}