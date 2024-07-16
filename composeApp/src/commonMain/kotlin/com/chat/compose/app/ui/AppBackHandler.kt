package com.chat.compose.app.ui

import androidx.compose.runtime.Composable

/**
 * @author liuzhongao
 * @since 2024/7/16 00:07
 */
@Composable
expect fun AppBackHandler(enable: Boolean, onBack: () -> Unit)