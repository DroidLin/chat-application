package com.chat.compose.app.lifecycle

import androidx.compose.runtime.*

/**
 * @author liuzhongao
 * @since 2024/6/30 19:09
 */
private val flagOfFirstFrameHasCalled = mutableStateOf(false)

@Composable
fun MainFirstFrameContent() {
    val called = remember { flagOfFirstFrameHasCalled }
    LaunchedEffect(called) {
        if (!called.value) {
            ApplicationLifecycleRegistry.onFirstFrameComplete()
            called.value = true
        }
    }
}