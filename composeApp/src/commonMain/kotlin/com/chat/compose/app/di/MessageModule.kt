package com.chat.compose.app.di

import com.application.channel.im.IMInitConfig
import com.application.channel.im.MsgClient
import com.application.channel.im.MsgConnectionService
import com.application.channel.message.MessageRepository
import dagger.Module
import dagger.Provides
import org.koin.dsl.module
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

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
        MsgClient.getService(MsgConnectionService::class.java).also { service ->
            val times = measureTimeMillis {
                val initConfig: IMInitConfig = get()
                service.initService(initConfig)
            }
            println("cost: $times ms")
        }
    }
    factory {
        val service: MsgConnectionService = get()
        service.messageRepository
    }
}
