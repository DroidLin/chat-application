package com.chat.compose.app.ui

import androidx.compose.material3.adaptive.Posture
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.window.core.layout.WindowSizeClass
import com.chat.compose.app.LocalWindowState

/**
 * @author liuzhongao
 * @since 2024/7/21 11:53
 */
@Composable
actual fun calculateWindowAdaptiveInfo(): WindowAdaptiveInfo {
    val windowState = LocalWindowState.current
    return remember {
        derivedStateOf {
            val widthDp = windowState.size.width
            val heightDp = windowState.size.height

            val windowSizeClass = WindowSizeClass.compute(widthDp.value, heightDp.value)
            WindowAdaptiveInfo(
                windowSizeClass = windowSizeClass,
                windowPosture = Posture(false, emptyList())
            )
        }
    }.value
}