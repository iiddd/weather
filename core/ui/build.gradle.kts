plugins {
    id("library")
    id("compose")
}
android { namespace = "com.iiddd.weather.core.ui" }

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.google.fonts)
}