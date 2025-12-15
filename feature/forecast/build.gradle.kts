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

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.graphics)
    debugImplementation(libs.compose.ui.tooling)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Testing
    testImplementation(project(":core:test-utils"))
    testImplementation(libs.mockito.kotlin)
}