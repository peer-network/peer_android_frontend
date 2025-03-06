// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.graphql).apply(false)
    alias(libs.plugins.kover).apply(true)
    alias(libs.plugins.ksp).apply(false)
}

subprojects {
    ext.set("android", listOf(":app"))
    beforeEvaluate {
        project(path) {
            apply("$rootDir/gradle/common.gradle")
        }
    }
}
