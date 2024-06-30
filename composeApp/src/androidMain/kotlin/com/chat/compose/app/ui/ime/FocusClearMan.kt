package com.chat.compose.app.ui.ime

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalFocusManager

/**
 * @author liuzhongao
 * @since 2024/6/21 21:30
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
actual fun FocusClearMan() {
    val focusRequester = LocalFocusManager.current
    val imeVisible = WindowInsets.isImeVisible
    LaunchedEffect(imeVisible) {
        if (!imeVisible) {
            focusRequester.clearFocus()
        }
    }
}