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
        create("jUnit") {
            id = "junit"
            implementationClass = "com.iiddd.weather.buildlogic.JunitPlugin"
        }
        create("library") {
            id = "library"
            implementationClass = "com.iiddd.weather.buildlogic.AndroidLibraryPlugin"
        }
        create("compose") {
            id = "compose"
            implementationClass = "com.iiddd.weather.buildlogic.AndroidComposePlugin"
        }
    }
}

dependencies {
    implementation(gradleApi())
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}