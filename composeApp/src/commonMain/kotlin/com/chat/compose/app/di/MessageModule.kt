package com.chat.compose.app.di

import com.application.channel.im.IMInitConfig
import com.application.channel.im.MsgClient
import com.application.channel.im.MsgConnectionService
import com.application.channel.message.MessageRepository
import dagger.Module
import dagger.Provides
import org.koin.dsl.module
import javax.inject.Singleton

/**
 * @author liuzhongao
 * @since 2024/6/16 21:13
 */

expect fun imInitConfig(): IMInitConfig

val messageModule = module {
    factory {
        imInitConfig()
    }
    factory {
        val initConfig: IMInitConfig = get()
        MsgClient.getService(MsgConnectionService::class.java).also { it.initService(initConfig) }
    }
    factory {
        val service: MsgConnectionService = get()
        service.messageRepository
    }
}
