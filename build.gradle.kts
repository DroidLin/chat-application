import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.android.library") version "8.1.1" apply false
    id("org.jetbrains.kotlin.multiplatform") version "1.9.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.0" apply false
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