package com.chat.compose.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import com.chat.compose.app.LocalWindow

/**
 * @author liuzhongao
 * @since 2024/7/17 00:43
 */
@Composable
actual fun rememberWindowInfo(): WindowInfo {
    val window = LocalWindow.current
    val density = LocalDensity.current
    val windowWidthDp = with(density) { window.width.toDp() }
    val windowHeightDp = with(density) { window.height.toDp() }

    return WindowInfo(
        screenWidthType = when {
            windowWidthDp.value < 600f -> WindowType.Compact
            windowWidthDp.value < 800f -> WindowType.Medium
            else -> WindowType.Expanded
        },
        screenHeightType = when {
            windowHeightDp.value < 400f -> WindowType.Compact
            windowHeightDp.value < 600f -> WindowType.Medium
            else -> WindowType.Expanded
        },
        screenWidth = windowWidthDp,
        screenHeight = windowHeightDp
    )
}