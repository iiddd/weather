package com.iiddd.weather.buildlogic

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class JunitPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        plugins.withId("com.android.library") { enableJUnitPlatform() }
        plugins.withId("com.android.application") { enableJUnitPlatform() }

        tasks.withType(Test::class.java).configureEach {
            useJUnitPlatform()
        }

        val libs = extensions.getByType<org.gradle.api.artifacts.VersionCatalogsExtension>()
            .named("libs")

        dependencies {
            add("testImplementation", platform(libs.findLibrary("junit-bom").get()))
            add("testImplementation", libs.findLibrary("junit-jupiter").get())
            add("testRuntimeOnly", libs.findLibrary("junit-platform-launcher").get())
        }
    }

    private fun Project.enableJUnitPlatform() {
        extensions.configure(BaseExtension::class.java) {
            testOptions.unitTests.all { it.useJUnitPlatform() }
        }
    }
}