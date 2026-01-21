import java.util.Properties

plugins {
    id("com.iiddd.weather.android.library")
    id("com.iiddd.weather.android.compose")
    id("com.iiddd.weather.android.junit")

    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.iiddd.weather.search"

    buildFeatures {
        resValues = true
    }

    defaultConfig {
        val props = Properties().apply {
            val f = rootProject.file("apikeys.properties")
            if (f.exists()) f.inputStream().use(::load)
        }
        val mapsKey = props.getProperty("GOOGLE_MAPS_API_KEY") ?: ""
        resValue("string", "google_maps_key", mapsKey)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":core:utils"))

    // UI extras
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)

    // Maps
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Testing
    testImplementation(project(":core:test-utils"))
    testImplementation(libs.mockito.kotlin)
}