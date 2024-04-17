plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
//    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
}

val dependenciesGroup: String by extra
val dependenciesName: String by extra
val dependenciesVersion: String by extra

group = dependenciesGroup
version = dependenciesVersion

kotlin {
    withSourcesJar()
    androidTarget {
        publishLibraryVariantsGroupedByFlavor = true
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    jvm("jvm") {
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            }
        }
    }

    sourceSets {
        val commonMain by getting
        commonMain.dependencies {
        }

        val jvmMain by getting
        jvmMain.dependencies {
        }

        val androidMain by getting
        androidMain.dependencies {
        }
    }
}

android {
    namespace = "com.android.dependencies.multiplatform.android"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
        targetSdk = 34
    }
}

//java {
//    sourceCompatibility = JavaVersion.VERSION_17
//    targetCompatibility = JavaVersion.VERSION_17
//    withJavadocJar()
//    withSourcesJar()
//}

publishing {
//    this.publications {
//        this.
//    }
//    publications {
//        register<MavenPublication>("java") {
//            groupId = dependenciesGroup
//            artifactId = dependenciesName
//            version = dependenciesVersion
//
//            afterEvaluate {
//                from(components["java"])
//            }
//        }
//    }
    repositories {
        maven {
            name = "repositoryLocalRepo"
            url = uri("${rootProject.projectDir}/repo")
        }
    }
}