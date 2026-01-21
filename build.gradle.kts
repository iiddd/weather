// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

tasks.register("preCommit") {
    group = "verification"
    description = "Runs fast checks before commit."

    val unitTestTasks = subprojects
        .flatMap { subproject -> subproject.tasks.matching { task -> task.name == "testDebugUnitTest" } }

    dependsOn(unitTestTasks)

    doFirst {
        if (unitTestTasks.isEmpty()) {
            logger.lifecycle("No testDebugUnitTest tasks found. Nothing to run.")
        }
    }
}