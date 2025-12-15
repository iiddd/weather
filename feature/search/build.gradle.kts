import java.util.Properties

plugins {
    id("library")
    id("compose")
    id("junit")

    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.iiddd.weather.search"

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
    implementation(project(":core:utils"))

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    debugImplementation(libs.compose.ui.tooling)

    // Google Maps
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Retrofit / OkHttp / Serialization
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)

    // Testing
    testImplementation(project(":core:test-utils"))
    testImplementation(libs.mockito.kotlin)
}