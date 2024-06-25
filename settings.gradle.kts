pluginManagement {
    repositories {
        maven { url = uri("${rootProject.projectDir}/repo") }
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("${rootProject.projectDir}/repo") }
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "chat-channel"
include(":jvm-only-module")
include(":android-only-module")
include(":multiplatform-module")

include(":app-channel-core")
include(":app-channel-client")
include(":app-channel-server")
include(":app-channel-im")
include(":app-channel-message-core")
include(":app-channel-message-client")
include(":app-channel-message-server")

include(":app-channel-backend-server")

include(":app-channel-database")

include(":composeApp")