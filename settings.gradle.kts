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

include(":feature:user:domain")
include(":feature:user:data")
include(":feature:user:remote")
include(":feature:user:ui")

include(":feature:blog:domain")
include(":feature:blog:data")
include(":feature:blog:remote")
include(":feature:blog:ui")

include(":feature:messaging:domain")
include(":feature:messaging:data")
include(":feature:messaging:remote")
include(":feature:messaging:ui")

include(":feature:wallet:domain")
include(":feature:wallet:data")
include(":feature:wallet:remote")
include(":feature:wallet:ui")

include(":feature:ads:domain")
include(":feature:ads:data")
include(":feature:ads:remote")
include(":feature:ads:ui")

include(":feature:social:domain")
include(":feature:social:data")
include(":feature:social:remote")
include(":feature:social:ui")

include(":app")
