package com.chat.compose.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import com.chat.compose.app.ui.BasicApplication
import com.chat.compose.app.ui.DesktopMaterialTheme
import com.chat.compose.app.ui.LoginApplication
import com.github.droidlin.composeapp.generated.resources.Res
import com.github.droidlin.composeapp.generated.resources.login_title
import org.jetbrains.compose.resources.stringResource

/**
 * @author liuzhongao
 * @since 2024/6/24 23:25
 */
@Composable
fun LoginWindow(
    onDismissRequest: () -> Unit,
) {
    Window(
        onCloseRequest = onDismissRequest,
        title = stringResource(Res.string.login_title),
        state = rememberWindowState(
            placement = WindowPlacement.Floating,
            position = WindowPosition.Aligned(Alignment.Center),
            size = DpSize(width = 400.dp, height = 600.dp)
        )
    ) {
        CompositionLocalProvider(LocalWindow provides this.window) {
            DesktopMaterialTheme(
                modifier = Modifier.fillMaxSize(),
            ) {
                LoginApplication()
            }
        }
    }
}


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