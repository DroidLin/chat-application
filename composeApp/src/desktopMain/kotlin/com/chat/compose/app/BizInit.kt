package com.chat.compose.app

import com.android.dependencies.common.ComponentFacade
import com.application.channel.core.client.TcpClientModule
import com.application.channel.message.ChatServiceModule
import com.chat.compose.app.di.LoginModule
import com.chat.compose.app.di.messageModule
import com.chat.compose.app.di.useCaseModule
import com.chat.compose.app.di.viewModelModule
import org.koin.core.context.startKoin

/**
 * @author liuzhongao
 * @since 2024/6/25 00:13
 */
fun initEntryPoint() {
    initSystemProperties()
    ComponentFacade.autoInit()
    initKoin()
    initAndStartChatService()
}

private fun initSystemProperties() {
    System.setProperty("apple.awt.application.appearance", "system")
    System.setProperty("apple.awt.application.name", "True")
}

private fun initKoin() {
    startKoin {
        modules(
            viewModelModule,
            messageModule,
            useCaseModule,
            TcpClientModule,
            ChatServiceModule,
            LoginModule
        )
    }
}