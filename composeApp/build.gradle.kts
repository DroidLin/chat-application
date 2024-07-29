import org.jetbrains.compose.desktop.application.dsl.TargetFormat

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.ksp)
}

val dependenciesGroup: String by extra
val dependenciesName: String by extra
val dependenciesVersion: String by extra

group = dependenciesGroup
version = dependenciesVersion

kotlin {
    androidTarget {
        publishLibraryVariantsGroupedByFlavor = true
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    jvm("desktop")

    sourceSets {
        val commonMain by getting
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.droidlin.common.jvm)
            implementation(project(":app-channel-im"))
            implementation(project(":app-channel-message-core"))

            implementation(libs.koin.core)
            implementation(libs.koin.coroutines)
            implementation(libs.koin.compose)

            implementation(libs.coil.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            implementation(libs.jetbrains.navigation.compose)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.moshi)

            implementation("androidx.compose.material3.adaptive:adaptive:1.0.0-beta04") {
                exclude("androidx.compose.foundation", "foundation-desktop")
            }
            implementation(libs.material3.adaptive.layout)
            implementation(libs.material3.adaptive.navigation)
            implementation(libs.window.core)
        }
        val androidMain by getting
        androidMain.dependencies {
            implementation(libs.androidx.activity.ktx)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.paging.compose)

            implementation(libs.droidlin.common.android)

            implementation(libs.koin.android)
            implementation(libs.koin.android.compat)
            implementation(libs.koin.androidx.compose)
            implementation(libs.androidx.lifecycle.process)
        }
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs) {
                exclude(group = "org.jetbrains.compose.material")
            }
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

dependencies {
//    implementation(libs.androidx.adaptive.navigation.desktop)
    ksp(libs.moshi.compiler)
}

android {
    namespace = "com.android.dependencies.chat.android"
    compileSdk = 34

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.android.dependencies.chat.android"
        compileSdk = 34
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/notice.txt"
            excludes += "META-INF/ASL2.0"
            excludes += "META-INF/*.kotlin_module"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/io.netty.versions.properties"
        }
    }
    signingConfigs {
        maybeCreate("debug").apply {
            this.keyAlias = "appKey"
            this.keyPassword = "123456"
            this.storePassword = "123456"
            this.storeFile = File(projectDir, "appKey")
        }
        maybeCreate("release").apply {
            this.keyAlias = "appKey"
            this.keyPassword = "123456"
            this.storePassword = "123456"
            this.storeFile = File(projectDir, "appKey")
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

compose.desktop {
    application {
        mainClass = "com.chat.compose.app.ApplicationKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.mplayer.app.compose"
            packageVersion = "1.0.0"
            macOS {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/AppIcon.icns"))
            }
            windows {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/launcher_icon.png"))
            }
            linux {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/launcher_icon.png"))
            }
        }
    }
}