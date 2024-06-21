package com.chat.compose.app.di

import com.application.channel.im.IMInitConfig
import com.application.channel.im.MsgClient
import com.application.channel.im.MsgConnectionService
import org.koin.dsl.module

/**
 * @author liuzhongao
 * @since 2024/6/16 21:13
 */

expect fun imInitConfig(): IMInitConfig

val messageModule = module {
    factory { imInitConfig() }
    factory {
        MsgClient.getService(MsgConnectionService::class.java).also { it.initService(get()) }
    }
    factory { get<MsgConnectionService>().messageRepository }
}
