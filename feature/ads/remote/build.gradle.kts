plugins { id(libs.plugins.graphql.get().pluginId) }

apollo {
    service("ads") {
        packageNamesFromFilePaths("ads")
        schemaFile.set(file("src/main/graphql/ads.schema.graphqls"))
        srcDir("src/main/graphql")
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:remote"))

    implementation(project(":feature:ads:domain"))
    implementation(project(":feature:ads:data"))

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
