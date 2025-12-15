plugins {
    id("library")
}
android { namespace = "com.iiddd.weather.core.testutils" }

dependencies {
    implementation(project(":core:utils"))
    api(libs.kotlinx.coroutines.test)
    implementation(libs.androidx.core.ktx)
}