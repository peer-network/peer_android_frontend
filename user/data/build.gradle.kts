
dependencies {
    implementation(project(":core:common"))
    implementation(project(":user:domain"))

    implementation(libs.gson)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
