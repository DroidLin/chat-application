package com.application.channel.core.server

import dagger.Binds
import dagger.Module

/**
 * @author liuzhongao
 * @since 2024/6/1 14:56
 */
@Module
internal interface CommonBindingModule {

    @Binds
    fun channelServer(impl: ChannelServerImpl): ChannelServer
}