plugins {
    id("library")
    id("compose")
}

android {
    namespace = "com.iiddd.weather.settings"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}