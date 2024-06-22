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

fun <T> Flow<T>.accessFirst(function: (T) -> Unit): Flow<T> {
    var first: Boolean = true
    return transform {
        if (first) {
            function(it)
            first = false
        }
        emit(it)
    }
}