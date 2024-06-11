import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.roomGradlePlugin) apply false
}

subprojects {
    val properties = Properties()
    val modulePropertiesFile = project.file("module.properties")
    if (modulePropertiesFile.exists()) {
        properties.load(FileInputStream(modulePropertiesFile))
        properties.forEach { (key, value) ->
            extraProperties.set(key.toString(), value)
        }
    } else println("missing module properties")
}