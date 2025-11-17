// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.android.build.gradle.BaseExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

subprojects {
    plugins.withId("com.android.library") {
        extensions.configure<BaseExtension>("android") {
            testOptions.unitTests.all { it.useJUnitPlatform() }
        }
    }
    plugins.withId("com.android.application") {
        extensions.configure<BaseExtension>("android") {
            testOptions.unitTests.all { it.useJUnitPlatform() }
        }
    }
}