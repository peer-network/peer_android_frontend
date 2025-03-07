plugins {
    id(libs.plugins.graphql.get().pluginId)
}

apollo {
    service("public") {
        packageNamesFromFilePaths("public")
        schemaFile.set(file("src/main/graphql/public.schema.graphqls"))
        srcDir("src/main/graphql/public")
    }

    service("protected") {
        packageNamesFromFilePaths("protected")
        schemaFile.set(file("src/main/graphql/protected.schema.graphqls"))
        srcDir("src/main/graphql/protected")
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:remote"))

    implementation(project(":user:domain"))
    implementation(project(":user:data"))

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
