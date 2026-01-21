package com.iiddd.weather.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidComposePlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        val versionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

        fun Project.configureComposeDependencies() {
            dependencies {
                add("implementation", platform(versionCatalog.findLibrary("compose-bom").get()))
                add("implementation", versionCatalog.findLibrary("compose-ui").get())
                add("implementation", versionCatalog.findLibrary("compose-ui-tooling-preview").get())
                add("debugImplementation", versionCatalog.findLibrary("compose-ui-tooling").get())
            }
        }

        plugins.withId("com.android.library") {
            extensions.configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }
            }
            configureComposeDependencies()
        }

        plugins.withId("com.android.application") {
            extensions.configure<ApplicationExtension> {
                buildFeatures {
                    compose = true
                }
            }
            configureComposeDependencies()
        }
    }
}