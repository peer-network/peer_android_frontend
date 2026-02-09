plugins {
    id(libs.plugins.ksp.get().pluginId)
    id(libs.plugins.gms.get().pluginId)
}

android {
    ndkVersion = "26.1.10909125"
    defaultConfig {
        versionCode = 40
        versionName = "1.12.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("int", "PAGING_LIMIT", "20")
        buildConfigField("String", "PLAYSTORE_URL", "\"https://play.google.com/store/apps/details?id=eu.peernetwork.app\"")
        buildConfigField("String","FEED_BACK","\"https://docs.google.com/forms/d/e/1FAIpQLSeTRecbfUTKmpYHSaE7bSawEagUpkOPagJtLqZdsec659HaGw/viewform\"")
        buildConfigField("String","PRIVACY","\"https://peerapp.de/privacy.html\"")
        buildConfigField("String","LICENCE","\"https://peerapp.de/EULA.html\"")
        buildConfigField("String","APP_WIKI","\"https://github.com/peer-network/peer_backend/wiki/Android\"")
        buildConfigField("String","BACKEND_WIKI","\"https://github.com/peer-network/peer_backend/wiki\"")
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            buildConfigField("String", "BASE_URL", "\"https://getpeer.eu\"")
            buildConfigField("String", "MEDIA_URL", "\"https://media.getpeer.eu\"")
            buildConfigField("boolean", "USE_SYSTEM_THEME", "false")
            buildConfigField("String","PRIVACY_POLICY_URL","\"https://www.freeprivacypolicy.com/live/02865c3a-79db-4baf-9ca1-7d91e2cf1724\"")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            buildConfigField("String", "BASE_URL", "\"https://peernetwork.eu\"")
            buildConfigField("String", "MEDIA_URL", "\"https://media.peernetwork.eu\"")
            buildConfigField("boolean", "USE_SYSTEM_THEME", "false")
            buildConfigField("String","PRIVACY_POLICY_URL","\"https://www.freeprivacypolicy.com/live/02865c3a-79db-4baf-9ca1-7d91e2cf1724\"")
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
    packaging {
        jniLibs {
            useLegacyPackaging = false
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:remote"))

    implementation(project(":persistence:domain"))
    implementation(project(":persistence:data"))
    implementation(project(":persistence:local"))

    implementation(project(":media:core"))
    implementation(project(":media:ui"))

    implementation(project(":feature:user:domain"))
    implementation(project(":feature:user:data"))
    implementation(project(":feature:user:remote"))
    implementation(project(":feature:user:ui"))

    implementation(project(":feature:blog:domain"))
    implementation(project(":feature:blog:data"))
    implementation(project(":feature:blog:remote"))
    implementation(project(":feature:blog:ui"))

    implementation(project(":feature:social:domain"))
    implementation(project(":feature:social:data"))
    implementation(project(":feature:social:remote"))
    implementation(project(":feature:social:ui"))

    implementation(project(":feature:ads:domain"))
    implementation(project(":feature:ads:data"))
    implementation(project(":feature:ads:remote"))
    implementation(project(":feature:ads:ui"))

    implementation(project(":feature:wallet:domain"))
    implementation(project(":feature:wallet:data"))
    implementation(project(":feature:wallet:remote"))
    implementation(project(":feature:wallet:ui"))

    implementation(project(":feature:messaging:domain"))
    implementation(project(":feature:messaging:data"))
    implementation(project(":feature:messaging:remote"))
    implementation(project(":feature:messaging:ui"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.functions)
    implementation(libs.firebase.store)

    implementation(libs.lottie)
    implementation(libs.markdown)

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
