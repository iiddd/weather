plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.iiddd.weather.search"
    compileSdk = 36

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    defaultConfig { minSdk = 31 }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Compose
    implementation(libs.compose.ui)
    implementation(platform(libs.compose.bom))

    implementation(libs.material)

    testImplementation(libs.junit)
}