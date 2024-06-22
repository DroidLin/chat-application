package com.application.channel.core.server

import dagger.Module
import dagger.Provides
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor
import javax.inject.Singleton

/**
 * @author liuzhongao
 * @since 2024/6/1 14:56
 */
@Module
class CommonModule {

    @Singleton
    @Provides
    fun provideChannelGroup(): ChannelGroup {
        return DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    }
}