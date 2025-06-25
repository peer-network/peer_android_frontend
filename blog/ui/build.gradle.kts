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
    implementation(project(":media:core"))

    implementation(project(":blog:domain"))
    implementation(libs.dagger)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.material)
    ksp(libs.dagger.compiler)
    implementation(libs.androidx.media3.exoplayer.v130)
    implementation(libs.androidx.media3.ui.v130)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.androidx.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
