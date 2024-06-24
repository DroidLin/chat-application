package com.chat.compose.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.application
import com.chat.compose.app.metadata.ApplicationConfiguration

/**
 * @author liuzhongao
 * @since 2024/6/16 10:36
 */
fun main() {
    initEntryPoint()
    application {
        val isSystemInDarkMode = isSystemInDarkTheme()
        val applicationConfiguration = remember(isSystemInDarkMode) {
            ApplicationConfiguration(isDarkMode = isSystemInDarkMode)
        }
        CompositionLocalProvider(
            LocalApplicationConfiguration provides applicationConfiguration
        ) {
            LoginWindow(::exitApplication)
//            AppWindow(::exitApplication)
        }
    }
}

val LocalWindow = compositionLocalOf<ComposeWindow> { error("not provided") }
val LocalApplicationConfiguration = compositionLocalOf<ApplicationConfiguration> { error("not provided") }