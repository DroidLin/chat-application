package com.application.channel.core.handler

import com.application.channel.core.ChannelEventListener
import com.application.channel.core.model.channelContext
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

/**
 * @author liuzhongao
 * @since 2024/5/18 18:39
 */
class ByteArrayHandler(
    private val channelEventListener: ChannelEventListener,
) : SimpleChannelInboundHandler<ByteArray>() {

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: ByteArray?) {
        if (ctx == null || msg == null) return
        this.channelEventListener.handleValueRead(ctx.channel().channelContext, msg)
    }

    override fun channelActive(ctx: ChannelHandlerContext?) {
        if (ctx == null) return
        this.channelEventListener.handleConnectionEstablished(ctx.channel().channelContext)
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        if (ctx == null) return
        this.channelEventListener.handleConnectionLoss(ctx.channel().channelContext)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        if (ctx == null) return
        this.channelEventListener.handleException(ctx.channel().channelContext, cause)
        super.exceptionCaught(ctx, cause)
    }
}