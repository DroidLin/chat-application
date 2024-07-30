package com.chat.compose.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.chat.compose.app.platform.ui.FrameworkScreen
import com.chat.compose.app.platform.ui.WindowAdaptiveInfoProvider
import com.chat.compose.app.ui.DesktopMaterialTheme

/**
 * @author liuzhongao
 * @since 2024/6/24 23:25
 */

val LocalWindowState = staticCompositionLocalOf<WindowState> { error("No WindowState") }

@Composable
fun AppWindow(
    onCloseRequest: () -> Unit,
) {
    val appConfiguration = LocalApplicationConfiguration.current
    val windowState = rememberWindowState(
        placement = WindowPlacement.Floating,
        position = WindowPosition.Aligned(Alignment.Center),
        size = DpSize(1200.dp, 800.dp),
    )
    Window(
        onCloseRequest = onCloseRequest,
        title = appConfiguration.title,
        state = windowState,
        alwaysOnTop = appConfiguration.alwaysOnTop
    ) {
        CompositionLocalProvider(
            LocalWindow provides this.window,
            LocalWindowState provides windowState
        ) {
            WindowAdaptiveInfoProvider {
                DesktopMaterialTheme(
                    modifier = Modifier.fillMaxSize()
                ) {
                    FrameworkScreen()
                }
            }
        }
    }
}