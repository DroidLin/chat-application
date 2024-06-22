package com.application.channel.message.modules

import com.application.channel.message.ChatServiceInitConfig
import dagger.Module
import dagger.Provides

/**
 * @author liuzhongao
 * @since 2024/6/22 11:38
 */
@Module
class InitialConfigModule(private val chatServiceInitConfig: ChatServiceInitConfig) {

    @Provides
    fun provideInitConfig(): ChatServiceInitConfig = this.chatServiceInitConfig
}