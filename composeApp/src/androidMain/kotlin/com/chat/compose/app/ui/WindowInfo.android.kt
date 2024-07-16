package com.chat.compose.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

/**
 * @author liuzhongao
 * @since 2024/7/17 00:43
 */
@Composable
actual fun rememberWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current
    val windowWidthDp = configuration.screenWidthDp
    val windowHeightDp = configuration.screenHeightDp
    return remember(windowWidthDp, windowHeightDp) {
        WindowInfo(
            screenWidthType = when {
                windowWidthDp < 600 -> WindowType.Compact
                windowWidthDp < 800 -> WindowType.Medium
                else -> WindowType.Expanded
            },
            screenHeightType = when {
                windowHeightDp < 400 -> WindowType.Compact
                windowHeightDp < 600 -> WindowType.Medium
                else -> WindowType.Expanded
            },
            screenWidth = windowWidthDp.dp,
            screenHeight = windowHeightDp.dp
        )
    }
}