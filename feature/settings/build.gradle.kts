plugins {
    id("com.iiddd.weather.android.library")
    id("com.iiddd.weather.android.compose")
}

android {
    namespace = "com.iiddd.weather.settings"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}