
android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    api(libs.core.ktx)
    api(libs.appcompat)
    api(libs.material)
    api(libs.viewmodel)
    api(libs.androidx.splash)
    api(libs.androidx.lifecyle)
    api(libs.androidx.activity.compose)

    api(libs.compose.ui)
    api(libs.compose.ui.graphics)
    api(libs.compose.ui.tooling)
    api(libs.compose.ui.tooling.preview)
    api(libs.compose.material3)
    api(libs.compose.viewmodel)
    api(libs.compose.livedata)
    api(libs.compose.constraintlayout)
    api(libs.compose.navigation)
    api(libs.compose.coil)

    api(libs.dagger.android)
}
