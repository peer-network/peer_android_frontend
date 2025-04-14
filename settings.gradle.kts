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
        maven("https://jitpack.io")
    }
}

rootProject.name = "Peer Network"

include(":core:common")
include(":core:remote")
include(":core:ui")

include(":persistence:domain")
include(":persistence:data")
include(":persistence:local")

include(":media:core")
include(":media:ui")

include(":user:domain")
include(":user:data")
include(":user:remote")
include(":user:ui")

include(":blog:domain")
include(":blog:data")
include(":blog:remote")
include(":blog:ui")

include(":social:domain")
include(":social:data")
include(":social:remote")
include(":social:ui")

include(":app")
