package com.chat.compose.app.init

import android.app.Application
import android.content.Context
import com.android.dependencies.common.ComponentFacade
import com.android.dependencies.common.android.installContext
import com.application.channel.core.client.TcpClientModule
import com.application.channel.message.ChatServiceModule
import com.chat.compose.app.AndroidChatService
import com.chat.compose.app.di.*
import com.chat.compose.app.initChatService
import com.chat.compose.app.lifecycle.ApplicationLifecycleObserver
import com.chat.compose.app.lifecycle.ApplicationLifecycleRegistry
import com.chat.compose.app.metadata.Profile
import kotlinx.coroutines.coroutineScope
import org.koin.core.context.startKoin

/**
 * @author liuzhongao
 * @since 2024/6/29 14:09
 */

fun initCore(applicationContext: Application) {
    initComponentFacade()
    installContext(applicationContext)
    initKoin()
    initApplicationLifecycleObserver(applicationContext)
    initNotificationChannel(applicationContext)
}

private fun initComponentFacade() {
    ComponentFacade.autoInit()
}

suspend fun initBiz() = coroutineScope {
//    listOf(
//        async { ComponentFacade.autoInit() },
//    ).awaitAll()
}
