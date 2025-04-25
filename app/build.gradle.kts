plugins {
    id(libs.plugins.ksp.get().pluginId)
    id(libs.plugins.gms.get().pluginId)
}

android {
    defaultConfig {
        versionCode = 2
        versionName = "1.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("int", "PAGING_LIMIT", "20")
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            buildConfigField("String", "BASE_URL", "\"https://getpeer.eu\"")
            buildConfigField("String", "MEDIA_URL", "\"https://media.getpeer.eu\"")
            buildConfigField("boolean", "USE_SYSTEM_THEME", "false")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            buildConfigField("String", "BASE_URL", "\"https://peernetwork.eu\"")
            buildConfigField("String", "MEDIA_URL", "\"https://media.peernetwork.eu\"")
            buildConfigField("boolean", "USE_SYSTEM_THEME", "false")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:remote"))

    implementation(project(":persistence:domain"))
    implementation(project(":persistence:data"))
    implementation(project(":persistence:local"))

    implementation(project(":user:domain"))
    implementation(project(":user:data"))
    implementation(project(":user:remote"))
    implementation(project(":user:ui"))

    implementation(project(":media:core"))
    implementation(project(":media:ui"))

    implementation(project(":blog:domain"))
    implementation(project(":blog:data"))
    implementation(project(":blog:remote"))
    implementation(project(":blog:ui"))

    implementation(project(":social:domain"))
    implementation(project(":social:data"))
    implementation(project(":social:remote"))
    implementation(project(":social:ui"))

    implementation(project(":wallet:domain"))
    implementation(project(":wallet:data"))
    implementation(project(":wallet:remote"))
    implementation(project(":wallet:ui"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)

    implementation(libs.lottie)

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
