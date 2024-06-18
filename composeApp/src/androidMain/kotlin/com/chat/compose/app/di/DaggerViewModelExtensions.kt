package com.chat.compose.app.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * @author liuzhongao
 * @since 2024/6/18 01:13
 */

@Composable
actual fun viewModelScopeComponent(): ViewModelScopeComponent {
    return remember {
        DaggerViewModelScopeComponent
            .builder().build()
    }
}
