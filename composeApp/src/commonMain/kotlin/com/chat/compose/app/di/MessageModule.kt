package com.chat.compose.app.di

import com.application.channel.im.IMInitConfig
import com.application.channel.im.MsgClient
import com.application.channel.im.MsgConnectionService
import com.application.channel.message.MessageRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author liuzhongao
 * @since 2024/6/16 21:13
 */
@Module
class MessageModule constructor(private val initConfig: IMInitConfig) {

    @Singleton
    @Provides
    fun provideMsgConnectionService(): MsgConnectionService {
        return MsgClient.getService(MsgConnectionService::class.java).also { service ->
            service.initService(this.initConfig)
        }
    }

    @Provides
    fun provideMessageRepository(msgConnectionService: MsgConnectionService): MessageRepository {
        return msgConnectionService.messageRepository
    }
}