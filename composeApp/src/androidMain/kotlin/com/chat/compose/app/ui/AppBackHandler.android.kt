package com.chat.compose.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

/**
 * @author liuzhongao
 * @since 2024/7/16 00:07
 */
@Composable
actual fun AppBackHandler(enable: Boolean, onBack: () -> Unit) {
    BackHandler(enabled = enable, onBack = onBack)
}