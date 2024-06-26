@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
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

application {
    mainClass = "com.app.channel.backend.server.KtorServerApplicationKt"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.ktor.core.jvm)
    implementation(libs.ktor.netty.jvm)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization.jackson)

    implementation(libs.logback)

    implementation(libs.koin.core)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.sqlite)
    ksp(libs.androidx.room.compiler)
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