plugins {
    id("weather.android.feature")
}

android {
    namespace = "com.iiddd.weather.settings"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
}