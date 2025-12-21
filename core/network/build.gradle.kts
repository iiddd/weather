plugins {
    id("library")
}
android { namespace = "com.iiddd.weather.core.network" }

dependencies {
    implementation(project(":core:utils"))

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