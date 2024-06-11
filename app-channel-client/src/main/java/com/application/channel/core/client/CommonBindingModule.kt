package com.application.channel.core.client

import dagger.Binds
import dagger.Module

/**
 * @author liuzhongao
 * @since 2024/6/1 11:47
 */
@Module
internal interface CommonBindingModule {
    @Binds
    fun instantiateChannelClient(client: ChannelClientImpl): ChannelClient
}