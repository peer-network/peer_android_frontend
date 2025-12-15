
dependencies {
    implementation(project(":core:common"))
    implementation(project(":user:domain"))
    implementation(project(":wallet:domain"))
    implementation(project(":blog:domain"))
    implementation(project(":persistence:domain"))

    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.androidx.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
