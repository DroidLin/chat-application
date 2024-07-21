@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlinPluginSpring)
    alias(libs.plugins.kotlinPluginJpa)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.ksp)
    alias(libs.plugins.roomGradlePlugin)
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
    implementation(libs.spring.boot.framework.starter.web)
    testImplementation(libs.spring.boot.framework.starter.test)
    implementation(libs.jetbrains.kotlinx.coroutines.core)
//    implementation(libs.jetbrains.kotlinx.coroutines.reactor)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.sqlite)
    ksp(libs.androidx.room.compiler)

    implementation(project(":app-channel-message-server"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

room {
    schemaDirectory("$projectDir/schemas")
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