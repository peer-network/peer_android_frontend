plugins { id(libs.plugins.graphql.get().pluginId) }

apollo {
    service("messaging") {
        packageNamesFromFilePaths("messaging")
        schemaFile.set(file("src/main/graphql/messaging.schema.graphqls"))
        srcDir("src/main/graphql")
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:remote"))

    implementation(project(":feature:messaging:domain"))
    implementation(project(":feature:messaging:data"))

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
