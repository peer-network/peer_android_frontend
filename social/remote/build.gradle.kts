plugins { id(libs.plugins.graphql.get().pluginId) }

apollo {
    service("social") {
        packageNamesFromFilePaths("social")
        schemaFile.set(file("src/main/graphql/social.schema.graphqls"))
        srcDir("src/main/graphql")
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:remote"))

    implementation(project(":social:domain"))
    implementation(project(":social:data"))

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
