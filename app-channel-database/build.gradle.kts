@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.roomGradlePlugin)
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
        sourceSets["jvmMain"].kotlin.srcDirs("build/generated/ksp/jvm/jvmMain/kotlin")
        sourceSets["androidMain"].kotlin.srcDirs("build/generated/ksp/android/androidMain/kotlin")

        val commonMain by getting
        commonMain.dependencies {
            api(libs.androidx.room.runtime)
            api(libs.androidx.sqlite)
        }

        val jvmMain by getting
        jvmMain.dependencies {
        }

        val androidMain by getting
        androidMain.dependencies {
            api(libs.androidx.room.runtime.android)
            api(libs.androidx.paging.runtime)
        }
    }
}

android {
    namespace = "com.android.dependencies.channel.database.android"
    compileSdk = 34

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = 21
        targetSdk = 34
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    ksp(libs.androidx.room.compiler)
}

publishing {
    repositories {
        maven {
            name = "repositoryLocalRepo"
            url = uri("${rootProject.projectDir}/repo")
        }
    }
}