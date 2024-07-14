package com.chat.compose.app.init

import com.application.channel.core.client.TcpClientModule
import com.application.channel.message.ChatServiceModule
import com.chat.compose.app.di.*
import org.koin.core.context.startKoin

/**
 * @author liuzhongao
 * @since 2024/7/14 10:59
 */

fun initKoin() {
    startKoin {
        modules(
            viewModelModule, messageModule, useCaseModule,
            TcpClientModule, ChatServiceModule, NetworkModule,
            SerializationModule
        )
    }
}
