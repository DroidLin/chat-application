package com.chat.compose.app.ui

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable

/**
 * @author liuzhongao
 * @since 2024/7/16 00:49
 */
@Composable
actual fun windowAdaptiveInfo(): WindowAdaptiveInfo {
    return currentWindowAdaptiveInfo()
}