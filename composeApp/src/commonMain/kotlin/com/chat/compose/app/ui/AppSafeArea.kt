package com.chat.compose.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.chat.compose.app.platform.ui.LocalWindowAdaptiveInfo

/**
 * @author liuzhongao
 * @since 2024/7/16 23:46
 */
val LocalAppSafeArea = compositionLocalOf<AppSafeArea> { AppSafeArea() }

@Stable
class AppSafeArea {

    var appNavigationSize: DpSize by mutableStateOf(DpSize.Zero)

    var navigationWidth: Dp by mutableStateOf(0.dp)
    var navigationHeight: Dp by mutableStateOf(0.dp)
}

@Stable
expect fun Modifier.applyAppSafeArea(): Modifier

@Stable
fun Modifier.appSafeAreaPadding(): Modifier  = composed {
    val windowAdaptiveInfo = LocalWindowAdaptiveInfo.current
    val appSafeArea = LocalAppSafeArea.current
    when (windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.EXPANDED, WindowWidthSizeClass.MEDIUM -> {
            val width = remember { derivedStateOf { appSafeArea.navigationWidth } }
            padding(start = width.value)
        }
        WindowWidthSizeClass.COMPACT -> {
            val height = remember { derivedStateOf { appSafeArea.navigationHeight } }
            padding(bottom = height.value)
        }
        else -> this
    }
}