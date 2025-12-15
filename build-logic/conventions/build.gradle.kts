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
        create("weatherJUnit") {
            id = "weather.junit"
            implementationClass = "com.iiddd.weather.buildlogic.WeatherJunitPlugin"
        }
        create("weatherAndroidFeature") {
            id = "weather.android.feature"
            implementationClass = "com.iiddd.weather.buildlogic.WeatherAndroidFeaturePlugin"
        }
    }
}

dependencies {
    implementation(gradleApi())
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}