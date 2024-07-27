package com.chat.compose.app.platform.ui

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable

/**
 * @author liuzhongao
 * @since 2024/7/21 11:53
 */
@Composable
actual fun calculateWindowAdaptiveInfo(): WindowAdaptiveInfo {
    return currentWindowAdaptiveInfo()
}