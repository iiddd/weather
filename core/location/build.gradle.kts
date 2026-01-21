plugins {
    id("com.iiddd.weather.android.library")
}

android { namespace = "com.iiddd.weather.core.location" }

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.play.services.location)
    implementation(libs.koin.android)
}