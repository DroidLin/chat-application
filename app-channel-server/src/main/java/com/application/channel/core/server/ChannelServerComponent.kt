package com.application.channel.core.server

import dagger.Component
import javax.inject.Singleton

/**
 * @author liuzhongao
 * @since 2024/6/1 14:57
 */
@Component(
    modules = [
        CommonModule::class,
        CommonBindingModule::class
    ]
)
interface ChannelServerComponent {

    fun channelServer(): ChannelServer
}