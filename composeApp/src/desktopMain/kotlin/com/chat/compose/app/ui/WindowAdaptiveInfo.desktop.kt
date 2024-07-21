package com.chat.compose.app.ui

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.Posture
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.chat.compose.app.LocalWindow
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

/**
 * @author liuzhongao
 * @since 2024/7/21 11:53
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
actual fun calculateWindowAdaptiveInfo(): WindowAdaptiveInfo {
    val window = LocalWindow.current
    var windowWidthDp by remember {
        val widthDp = window.width.dp
        mutableStateOf(widthDp)
    }
    var windowHeightDp by remember {
        val heightDp = window.height.dp
        mutableStateOf(heightDp)
    }

    val componentListener = remember {
        object : ComponentAdapter() {
            override fun componentResized(p0: ComponentEvent?) {
                windowWidthDp = window.width.dp
                windowHeightDp = window.height.dp
            }
        }
    }

    DisposableEffect(Unit) {
        window.addComponentListener(componentListener)
        onDispose {
            window.removeComponentListener(componentListener)
        }
    }

    return remember {
        derivedStateOf {
            WindowAdaptiveInfo(
                windowSizeClass = WindowSizeClass.compute(windowWidthDp.value, windowHeightDp.value),
                windowPosture = Posture(false, emptyList())
            )
        }
    }.value
}