package com.chat.compose.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp

/**
 * @author liuzhongao
 * @since 2024/7/17 00:43
 */
val LocalAppWindowInfo = compositionLocalOf<WindowInfo> { error("No WindowInfo Provided.") }

@Composable
expect fun rememberWindowInfo(): WindowInfo

@Stable
data class WindowInfo(
    val screenWidthType: WindowType,
    val screenHeightType: WindowType,
    val screenWidth: Dp,
    val screenHeight: Dp,
)

@Stable
sealed interface WindowType {
    @Stable
    data object Compact : WindowType

    @Stable
    data object Medium : WindowType

    @Stable
    data object Expanded : WindowType
}