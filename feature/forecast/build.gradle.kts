import java.util.Properties


plugins {
    id("library")
    id("compose")
    id("junit")

    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.iiddd.weather.forecast"

    defaultConfig {
        val props = Properties().apply {
            val f = rootProject.file("apikeys.properties")
            if (f.exists()) f.inputStream().use(::load)
        }
        val openWeather = props.getProperty("OPEN_WEATHER_API_KEY") ?: ""
        buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"$openWeather\"")
    }
}

dependencies {
    implementation(project(":core:location"))
    implementation(project(":core:ui"))
    implementation(project(":core:utils"))

    implementation(libs.androidx.core.ktx)

    // UI extras
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.graphics)

    // Network / etc
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Testing
    testImplementation(project(":core:test-utils"))
    testImplementation(libs.mockito.kotlin)
}