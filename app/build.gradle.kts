plugins {
    id("com.iiddd.weather.android.application")
    id("com.iiddd.weather.android.compose")

    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.iiddd.weather"

    defaultConfig {
        applicationId = "com.iiddd.weather"
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.location)
    implementation(projects.core.preferences)
    implementation(projects.feature.forecast)
    implementation(projects.feature.search)
    implementation(projects.feature.settings)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.material)

    // Compose
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.material3)
    implementation(libs.compose.activity)
    implementation(libs.compose.material.icons.extended)

    // Navigation3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    // KotlinX Serialization
    implementation(libs.kotlinx.serialization.json)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    debugImplementation(libs.compose.ui.test.manifest)
}