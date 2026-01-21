plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "com.iiddd.weather.buildlogic"

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin {
    plugins {
        create("androidApplication") {
            id = "com.iiddd.weather.android.application"
            implementationClass = "com.iiddd.weather.buildlogic.AndroidApplicationPlugin"
        }
        create("androidCompose") {
            id = "com.iiddd.weather.android.compose"
            implementationClass = "com.iiddd.weather.buildlogic.AndroidComposePlugin"
        }
        create("androidJunit") {
            id = "com.iiddd.weather.android.junit"
            implementationClass = "com.iiddd.weather.buildlogic.JunitPlugin"
        }
        create("androidLibrary") {
            id = "com.iiddd.weather.android.library"
            implementationClass = "com.iiddd.weather.buildlogic.AndroidLibraryPlugin"
        }
    }
}

dependencies {
    implementation(gradleApi())
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}