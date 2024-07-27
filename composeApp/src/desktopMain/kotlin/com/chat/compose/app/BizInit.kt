package com.chat.compose.app

import com.android.dependencies.common.ComponentFacade
import com.application.channel.core.client.TcpClientModule
import com.application.channel.core.util.koinInject
import com.application.channel.message.ChatServiceModule
import com.chat.compose.app.di.*
import com.chat.compose.app.lifecycle.ApplicationLifecycleObserver
import com.chat.compose.app.lifecycle.ApplicationLifecycleRegistry
import com.chat.compose.app.metadata.Profile
import com.chat.compose.app.services.ProfileService
import com.chat.compose.app.util.PreferenceCenter
import org.koin.core.context.startKoin

/**
 * @author liuzhongao
 * @since 2024/6/25 00:13
 */
fun initEntryPoint() {
    initSystemProperties()
    initKoin()
    initApplicationLifecycleObserver()
    bizInit()
}

private fun initSystemProperties() {
    System.setProperty("apple.awt.application.appearance", "system")
    System.setProperty("apple.awt.application.name", "True")
    System.setProperty("skiko.renderApi", "OPENGL")
}

private fun initApplicationLifecycleObserver() {
    ApplicationLifecycleRegistry.addObserver(
        object : ApplicationLifecycleObserver {
            override suspend fun onUserLogin(profile: Profile) {
                initAndStartChatService()
            }

            override suspend fun onUserLogout() {
                stopChatService()
                PreferenceCenter.logout()
                koinInject<ProfileService>().logout()
            }

            override suspend fun onFirstFrameComplete() {
                initAndStartChatService()
            }
        }
    )
}

fun bizInit() {
    ComponentFacade.autoInit()
}

private fun initKoin() {
    startKoin {
        modules(
            viewModelModule,
            messageModule,
            useCaseModule,
            TcpClientModule,
            ChatServiceModule,
            NetworkModule,
            SerializationModule
        )
    }
}