package com.chat.compose.app.platform.ui

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

/**
 * @author liuzhongao
 * @since 2024/7/21 11:53
 */
val LocalWindowAdaptiveInfo = compositionLocalOf<WindowAdaptiveInfo> { error("No WindowAdaptiveInfoProvider provided") }

@Composable
fun WindowAdaptiveInfoProvider(content: @Composable () -> Unit) {
    val windowAdaptiveInfo = calculateWindowAdaptiveInfo()
    CompositionLocalProvider(LocalWindowAdaptiveInfo provides windowAdaptiveInfo, content = content)
}

@Composable
expect fun calculateWindowAdaptiveInfo(): WindowAdaptiveInfo