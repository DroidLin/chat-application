package com.chat.compose.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.chat.compose.app.LocalApplicationConfiguration
import com.chat.compose.app.LocalWindow
import org.koin.compose.KoinContext

/**
 * @author liuzhongao
 * @since 2024/6/16 13:41
 */
@Composable
fun DesktopMaterialTheme(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val window = LocalWindow.current
    val windowConfiguration = LocalApplicationConfiguration.current
    val (lightColor, darkColor) = remember { YellowTheme }

    val colorScheme by remember {
        derivedStateOf {
            if (windowConfiguration.isDarkMode) {
                darkColor
            } else lightColor
        }
    }

    LaunchedEffect(colorScheme) {
        window.background = java.awt.Color(colorScheme.background.toArgb())
        window.foreground = null
        window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
        window.rootPane.putClientProperty("apple.awt.fullWindowContent", true)
    }

    MaterialTheme(
        colorScheme = colorScheme,
    ) {
        KoinContext {
            Surface(modifier = modifier, content = content)
        }
    }
}