package com.application.channel.message.modules

import com.application.channel.message.MessageRepository
import com.application.channel.message.MessageRepositoryImpl
import dagger.Binds
import dagger.Module

/**
 * @author liuzhongao
 * @since 2024/6/10 14:00
 */
@Module
internal interface MessageRepositoryModule {

    @Binds
    fun provideMessageRepository(impl: MessageRepositoryImpl): MessageRepository
}