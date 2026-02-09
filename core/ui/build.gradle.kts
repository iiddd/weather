plugins {
    id("com.iiddd.weather.android.library")
    id("com.iiddd.weather.android.compose")
}
android { namespace = "com.iiddd.weather.core.ui" }

dependencies {
    api(projects.core.theme)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.compose.material3)
}