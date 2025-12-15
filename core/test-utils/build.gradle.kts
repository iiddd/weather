plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.iiddd.weather.core.testutils"
    compileSdk = 36

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    defaultConfig { minSdk = 31 }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":core:utils"))

    api(libs.kotlinx.coroutines.test)

    implementation(libs.androidx.core.ktx)
}