package com.application.channel.core.handler.codc

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.group.ChannelGroup

/**
 * @author liuzhongao
 * @since 2024/5/12 13:04
 */
class ChannelCollectorCodec(
    private val channelGroup: ChannelGroup
) : ChannelDuplexHandler() {

    override fun handlerAdded(ctx: ChannelHandlerContext?) {
        if (ctx == null) return
        this.channelGroup.add(ctx.channel())
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        if (ctx == null) return
        this.channelGroup.remove(ctx.channel())
    }
}