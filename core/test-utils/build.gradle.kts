plugins {
    id("com.iiddd.weather.android.library")
}
android { namespace = "com.iiddd.weather.core.testutils" }

dependencies {
    implementation(projects.core.utils)
    api(libs.kotlinx.coroutines.test)
    implementation(libs.androidx.core.ktx)
}