package com.chat.compose.app

import android.app.Application
import android.content.Context
import com.android.dependencies.common.ComponentFacade
import com.android.dependencies.common.android.installContext
import com.application.channel.core.client.TcpClientModule
import com.application.channel.message.ChatServiceModule
import com.chat.compose.app.di.*
import com.chat.compose.app.lifecycle.ApplicationLifecycleObserver
import com.chat.compose.app.lifecycle.ApplicationLifecycleRegistry
import com.chat.compose.app.metadata.Profile
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.koin.core.context.startKoin

/**
 * @author liuzhongao
 * @since 2024/6/29 14:09
 */

fun initCore(applicationContext: Application) {
    installContext(applicationContext)
    initKoin()
    initApplicationLifecycleObserver(applicationContext)
}

private fun initKoin() {
    startKoin {
        modules(
            viewModelModule, messageModule, useCaseModule,
            TcpClientModule, ChatServiceModule, NetworkModule,
            SerializationModule
        )
    }
}

private fun initApplicationLifecycleObserver(applicationContext: Context) {
    val observer = object : ApplicationLifecycleObserver {
        override suspend fun onUserLogin(profile: Profile) {
            initChatService()
            AndroidChatService.start(applicationContext)
        }

        override suspend fun onUserLogout() {
            AndroidChatService.stop(applicationContext)
        }

        override suspend fun onFirstFrameComplete() {
            initChatService()
            AndroidChatService.start(applicationContext)
        }
    }
    ApplicationLifecycleRegistry.addObserver(observer)
}

suspend fun initBiz() = coroutineScope {
    listOf(
        async { ComponentFacade.autoInit() },
    ).awaitAll()
}
