@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ksp)
    id("maven-publish")
}

val dependenciesGroup: String by extra
val dependenciesName: String by extra
val dependenciesVersion: String by extra

group = dependenciesGroup
version = dependenciesVersion

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    api(project(":app-channel-core"))
    api(project(":app-channel-message-core"))
    api(project(":app-channel-client"))
    api(project(":app-channel-database"))
    ksp(libs.dagger.compiler)
}

publishing {
    publications {
        register<MavenPublication>("java") {
            groupId = dependenciesGroup
            artifactId = dependenciesName
            version = dependenciesVersion

            afterEvaluate {
                from(components["java"])
            }
        }
    }
    repositories {
        maven {
            name = "repositoryLocalRepo"
            url = uri("${rootProject.projectDir}/repo")
        }
    }
}