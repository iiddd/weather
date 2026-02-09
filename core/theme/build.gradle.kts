plugins {
    id("com.iiddd.weather.android.library")
    id("com.iiddd.weather.android.compose")
}

android { namespace = "com.iiddd.weather.core.theme" }

dependencies {
    implementation(libs.compose.material3)
}

