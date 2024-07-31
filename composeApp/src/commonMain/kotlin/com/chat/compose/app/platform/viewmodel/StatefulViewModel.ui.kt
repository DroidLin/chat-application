package com.chat.compose.app.platform.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach

/**
 * @author liuzhongao
 * @since 2024/8/1 00:28
 */
@Composable
fun <E : Event> AbstractStatefulViewModel<*, E>.ReceiveIntentEffect(function: suspend (E) -> Unit) {
    LaunchedEffect(this, function) {
        eventFlow
            .distinctUntilChanged()
            .onEach(function)
            .collect()
    }
}