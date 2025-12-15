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
    }
}

dependencies {
    implementation(gradleApi())
    implementation(libs.android.gradlePlugin)
}