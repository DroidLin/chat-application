package com.chat.compose.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.chat.compose.app.metadata.WindowConfiguration
import com.chat.compose.app.ui.DesktopMaterialTheme
import com.chat.compose.app.ui.screen.BasicApplication

/**
 * @author liuzhongao
 * @since 2024/6/16 10:36
 */
fun main() {
    application {
        val isSystemInDarkMode = isSystemInDarkTheme()
        val windowConfiguration = remember(isSystemInDarkMode) {
            WindowConfiguration(isDarkMode = isSystemInDarkMode)
        }
        CompositionLocalProvider(
            LocalWindowConfiguration provides windowConfiguration
        ) {
            Window(
                onCloseRequest = ::exitApplication,
                title = windowConfiguration.title,
                undecorated = false,
                transparent = false,
                state = rememberWindowState(
                    placement = WindowPlacement.Floating,
                    position = WindowPosition.Aligned(Alignment.Center),
                    size = DpSize(1000.dp, 800.dp)
                )
            ) {
                CompositionLocalProvider(LocalWindow provides this.window) {
                    DesktopMaterialTheme(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        BasicApplication()
                    }
                }
            }
        }
    }
}

val LocalWindow = compositionLocalOf<ComposeWindow> { error("not provided") }
val LocalWindowConfiguration = compositionLocalOf<WindowConfiguration> { error("not provided") }