package com.chat.compose.app.di

import com.application.channel.database.AppMessageDatabase
import com.application.channel.im.*
import com.application.channel.message.meta.MessageParser
import org.koin.dsl.module

/**
 * @author liuzhongao
 * @since 2024/6/16 21:13
 */

expect fun platformAppDatabaseFactory(): AppMessageDatabase.Factory

expect fun imInitConfig(): IMInitConfig
expect fun imDatabaseInitConfig(): IMDatabaseInitConfig

val messageModule = module {
    factory { imInitConfig() }
    factory { imDatabaseInitConfig() }
    factory { SingleIMManager.connectionService }
    factory { SingleIMManager.connectionService.messageRepository }
    factory { SingleIMManager.msgService }
    factory { MessageParser() }
}
