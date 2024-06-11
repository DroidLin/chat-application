package com.application.channel.core.client

import dagger.Component
import javax.inject.Singleton

/**
 * @author liuzhongao
 * @since 2024/6/1 11:34
 */
@Component(
    modules = [
        CommonModule::class,
        CommonBindingModule::class
    ]
)
interface ChannelClientComponent {

    fun channelClient(): ChannelClient
}