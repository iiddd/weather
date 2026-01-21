package com.iiddd.weather.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

class JunitPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {

        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }

        val versionCatalog = extensions.getByType<VersionCatalogsExtension>()
            .named("libs")

        dependencies {
            add("testImplementation", platform(versionCatalog.findLibrary("junit-bom").get()))
            add("testImplementation", versionCatalog.findLibrary("junit-jupiter").get())
            add("testRuntimeOnly", versionCatalog.findLibrary("junit-platform-launcher").get())
        }
    }
}