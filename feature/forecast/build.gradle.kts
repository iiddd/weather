import java.util.Properties


plugins {
    id("com.iiddd.weather.android.library")
    id("com.iiddd.weather.android.compose")
    id("com.iiddd.weather.android.junit")

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
    // Modules
    implementation(projects.core.location)
    implementation(projects.core.network)
    implementation(projects.core.ui)
    implementation(projects.core.utils)

    //Lifecycle
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

    // Testing
    testImplementation(projects.core.testUtils)
    testImplementation(libs.mockito.kotlin)
}