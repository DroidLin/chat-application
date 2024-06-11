package com.application.channel.core

import io.netty.channel.ChannelHandler.Sharable

/**
 * @author liuzhongao
 * @since 2024/5/31 00:23
 */
@Sharable
class SimpleDataHandler(
    private val channelEventListener: SocketChannelEventListener
) : DataHandler<Any>(), SocketChannelEventListener by channelEventListener