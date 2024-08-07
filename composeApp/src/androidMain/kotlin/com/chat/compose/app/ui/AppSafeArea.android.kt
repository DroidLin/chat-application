package com.chat.compose.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.chat.compose.app.platform.ui.LocalWindowAdaptiveInfo

/**
 * @author liuzhongao
 * @since 2024/7/16 23:46
 */
@Stable
actual fun Modifier.applyAppSafeArea(): Modifier = composed {
    val appSafeArea = LocalAppSafeArea.current
    val density = LocalDensity.current
    DisposableEffect(Unit) {
        onDispose {
            appSafeArea.navigationWidth = 0.dp
            appSafeArea.navigationHeight = 0.dp
        }
    }
    onSizeChanged { size ->
        appSafeArea.navigationWidth = with(density) { size.width.toDp() }
        appSafeArea.navigationHeight = with(density) { size.height.toDp() }
        appSafeArea.appNavigationSize = with(density) {
            DpSize(size.width.toDp(), size.height.toDp())
        }
    }
}