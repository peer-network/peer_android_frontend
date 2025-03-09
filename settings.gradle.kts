pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Peer Network"

include(":core:common")
include(":core:remote")
include(":core:ui")

include(":persistence:domain")
include(":persistence:data")
include(":persistence:local")

include(":user:domain")
include(":user:data")
include(":user:remote")

include(":social:domain")
include(":social:data")
include(":social:remote")

include(":app")
