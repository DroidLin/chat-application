package com.chat.compose.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.chat.compose.app.screen.FrameworkScreen
import com.chat.compose.app.ui.*

/**
 * @author liuzhongao
 * @since 2024/6/24 23:25
 */
@Composable
fun AppWindow(
    onCloseRequest: () -> Unit,
) {
    val appConfiguration = LocalApplicationConfiguration.current
    Window(
        onCloseRequest = onCloseRequest,
        title = appConfiguration.title,
        state = rememberWindowState(
            placement = WindowPlacement.Floating,
            position = WindowPosition.Aligned(Alignment.Center),
            size = DpSize(800.dp, 600.dp),
        )
    ) {
        CompositionLocalProvider(LocalWindow provides this.window) {
            DesktopMaterialTheme(
                modifier = Modifier.fillMaxSize()
            ) {
                FrameworkScreen()
            }
        }
    }
}