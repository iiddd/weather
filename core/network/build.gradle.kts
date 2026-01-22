plugins {
    id("com.iiddd.weather.android.library")
}
android { namespace = "com.iiddd.weather.core.network" }

dependencies {
    implementation(projects.core.utils)

    // Retrofit / OkHttp / Serialization (shared across app)
    api(libs.retrofit)
    api(libs.retrofit.serialization)
    api(libs.okhttp)
    api(libs.okhttp.logging.interceptor)
    api(libs.kotlinx.serialization.json)

    // AndroidX
    implementation(libs.androidx.core.ktx)

    testImplementation(libs.kotlinx.coroutines.test)
}