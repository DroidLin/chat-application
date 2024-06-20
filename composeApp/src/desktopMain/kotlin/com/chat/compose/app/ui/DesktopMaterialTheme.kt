package com.chat.compose.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.chat.compose.app.LocalApplicationConfiguration
import com.chat.compose.app.LocalWindow
import com.chat.compose.app.di.messageModule
import com.chat.compose.app.di.useCaseModule
import com.chat.compose.app.di.viewModelModule
import moe.tlaster.precompose.PreComposeApp
import org.koin.compose.KoinApplication

/**
 * @author liuzhongao
 * @since 2024/6/16 13:41
 */
@Composable
fun DesktopMaterialTheme(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val window = LocalWindow.current
    val windowConfiguration = LocalApplicationConfiguration.current
    val (light, dark) = remember { YellowTheme }

    val colorScheme by remember {
        derivedStateOf {
            if (windowConfiguration.isDarkMode) {
                dark
            } else light
        }
    }

    LaunchedEffect(colorScheme) {
        window.background = java.awt.Color(colorScheme.background.toArgb())
        window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
        System.setProperty("apple.awt.application.appearance", "system")
    }

    MaterialTheme(
        colorScheme = colorScheme,
    ) {
        PreComposeApp {
            KoinApplication(
                application = {
                    modules(
                        viewModelModule,
                        messageModule,
                        useCaseModule,
                    )
                }
            ) {
                Surface(modifier = modifier, content = content)
            }
        }
    }
}