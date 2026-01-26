plugins {
    id("com.iiddd.weather.android.library")
}

android {
    namespace = "com.iiddd.weather.core.preferences"
}

dependencies {
    implementation(projects.core.ui)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.koin.android)
}