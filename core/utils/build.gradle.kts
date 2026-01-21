plugins {
    id("com.iiddd.weather.android.library")
}
android { namespace = "com.iiddd.weather.core.utils" }

dependencies {
    implementation(libs.androidx.core.ktx)
}