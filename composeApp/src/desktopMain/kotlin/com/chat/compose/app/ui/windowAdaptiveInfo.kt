package com.chat.compose.app.ui

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.HingeInfo
import androidx.compose.material3.adaptive.Posture
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.calculateThreePaneScaffoldValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.toSize
import androidx.window.core.layout.WindowSizeClass
import com.chat.compose.app.LocalWindow

/**
 * @author liuzhongao
 * @since 2024/7/16 00:49
 */
@Composable
actual fun windowAdaptiveInfo(): WindowAdaptiveInfo {
    val window = LocalWindow.current
    val density = LocalDensity.current
    val windowDpSize = remember(density, window) {
        derivedStateOf {
            with(density) {
                val width = window.width.toDp()
                val height = window.height.toDp()
                DpSize(width, height)
            }
        }
    }
    val windowSizeClass = WindowSizeClass.compute(
        dpWidth = windowDpSize.value.width.value,
        dpHeight = windowDpSize.value.height.value
    )
    return WindowAdaptiveInfo(windowSizeClass, Posture())
}