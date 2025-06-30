plugins { id(libs.plugins.ksp.get().pluginId) }

android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":persistence:domain"))

    implementation(project(":media:core"))

    implementation(libs.permissions)
    implementation(libs.media3)
    implementation(libs.ucrop)
    implementation(libs.lrucache)
    implementation(libs.blur)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.androidx.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
