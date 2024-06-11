package com.application.channel.core.client

import dagger.Module
import dagger.Provides
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor
import javax.inject.Singleton

/**
 * @author liuzhongao
 * @since 2024/6/1 11:35
 */
@Module
internal class CommonModule {

    @Provides
    fun provideChannelGroup(): ChannelGroup {
        return DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    }
}