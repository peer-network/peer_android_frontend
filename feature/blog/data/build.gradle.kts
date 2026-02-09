
dependencies {
    implementation(project(":core:common"))
    implementation(project(":persistence:domain"))

    implementation(project(":feature:user:domain"))
    implementation(project(":feature:wallet:domain"))
    implementation(project(":feature:blog:domain"))

    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.androidx.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
