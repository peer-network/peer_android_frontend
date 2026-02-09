plugins { id(libs.plugins.graphql.get().pluginId) }

apollo {
    service("blog") {
        packageNamesFromFilePaths()
        schemaFile.set(file("src/main/graphql/blog.schema.graphqls"))
        srcDir("src/main/graphql")
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:remote"))

    implementation(project(":feature:blog:domain"))
    implementation(project(":feature:blog:data"))

    implementation(libs.apache)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
