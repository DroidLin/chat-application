package com.application.channel.core.initializer

import com.application.channel.core.model.DatagramChannelInitConfig
import com.application.channel.core.model.InitConfig
import com.application.channel.core.model.MultiInitConfig
import com.application.channel.core.model.SocketChannelInitConfig
import io.netty.channel.group.ChannelGroup

/**
 * @author liuzhongao
 * @since 2024/5/26 23:20
 */
object ChannelInitializerFactory {

    fun create(channelGroup: ChannelGroup, initConfig: InitConfig): ChannelInitializer {
        return when (initConfig) {
            is SocketChannelInitConfig -> SocketChannelInitializer(channelGroup, initConfig)
            is MultiInitConfig -> MultiChannelInitializer(channelGroup, initConfig)
            is DatagramChannelInitConfig -> DatagramChannelInitializer(channelGroup, initConfig)
            else -> throw IllegalArgumentException("unrecognized init config type: ${initConfig.javaClass.name}")
        }
    }
}