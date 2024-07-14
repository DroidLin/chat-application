package com.chat.compose.app.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

/**
 * @author liuzhongao
 * @since 2024/6/20 21:55
 */
fun <T> Flow<T>.collect(coroutineScope: CoroutineScope): Job {
    return coroutineScope.launch(Dispatchers.Default) { collect() }
}

fun <T> Flow<T>.onFirst(function: (T) -> Unit): Flow<T> {
    var hasAccess: Boolean = true
    return transform {
        if (hasAccess) {
            function(it)
            hasAccess = false
        }
        emit(it)
    }
}