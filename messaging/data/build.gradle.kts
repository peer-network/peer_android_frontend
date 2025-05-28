
dependencies {
    implementation(project(":core:common"))
    implementation(project(":wallet:domain"))

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
