plugins { id(libs.plugins.graphql.get().pluginId) }

apollo {
    service("wallet") {
        packageNamesFromFilePaths("wallet")
        schemaFile.set(file("src/main/graphql/wallet.schema.graphqls"))
        srcDir("src/main/graphql")
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:remote"))

    implementation(project(":wallet:domain"))
    implementation(project(":wallet:data"))

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
