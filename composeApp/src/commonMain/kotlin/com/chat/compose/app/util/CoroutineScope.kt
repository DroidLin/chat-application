package com.chat.compose.app.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob

/**
 * @author liuzhongao
 * @since 2024/6/20 21:54
 */

val mainCoroutineScope by lazy { MainScope() }
val defaultCoroutineScope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
val ioCoroutineScope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.Default) }