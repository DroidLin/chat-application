pluginManagement {
    repositories {
        maven { url = uri("${rootProject.projectDir}/repo") }
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("${rootProject.projectDir}/repo") }
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}


rootProject.name = "storage"
include(":android-only-module")
include(":storage")
include(":jvm-only-module")