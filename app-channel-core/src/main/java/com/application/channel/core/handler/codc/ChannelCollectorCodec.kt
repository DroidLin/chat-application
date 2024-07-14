package com.application.channel.core.handler.codc

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.group.ChannelGroup
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author liuzhongao
 * @since 2024/5/12 13:04
 */
class ChannelCollectorCodec(
    private val channelGroup: ChannelGroup
) : ChannelDuplexHandler() {

    private val logger = Logger.getLogger("ChannelCollectorCodec")

    override fun handlerAdded(ctx: ChannelHandlerContext?) {
        if (ctx == null) return
        val channel = ctx.channel()
        this.channelGroup.add(channel)
        this.logger.log(Level.INFO, "channel connected: ${channel.remoteAddress()}")
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        if (ctx == null) return
        val channel = ctx.channel()
        this.channelGroup.remove(channel)
        this.logger.log(Level.INFO, "channel connection loss: ${channel.remoteAddress()}")
    }
}