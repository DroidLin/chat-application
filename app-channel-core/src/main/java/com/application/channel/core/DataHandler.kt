package com.application.channel.core

import com.application.channel.core.model.channelContext
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

/**
 * @author liuzhongao
 * @since 2024/5/21 01:08
 */
abstract class DataHandler<T : Any?> : SimpleChannelInboundHandler<T>(), SocketChannelEventListener {

    final override fun handlerAdded(ctx: ChannelHandlerContext?) {
        super.handlerAdded(ctx)
    }

    final override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        super.handlerRemoved(ctx)
    }

    final override fun ensureNotSharable() {
        super.ensureNotSharable()
    }

    final override fun isSharable(): Boolean {
        return super.isSharable()
    }

    final override fun channelRegistered(ctx: ChannelHandlerContext?) {
        super.channelRegistered(ctx)
    }

    final override fun channelUnregistered(ctx: ChannelHandlerContext?) {
        super.channelUnregistered(ctx)
    }

    final override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        super.channelRead(ctx, msg)
    }

    final override fun channelReadComplete(ctx: ChannelHandlerContext?) {
        super.channelReadComplete(ctx)
    }

    final override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        super.userEventTriggered(ctx, evt)
    }

    final override fun channelWritabilityChanged(ctx: ChannelHandlerContext?) {
        super.channelWritabilityChanged(ctx)
    }

    final override fun acceptInboundMessage(msg: Any?): Boolean {
        return super.acceptInboundMessage(msg)
    }

    final override fun channelRead0(ctx: ChannelHandlerContext?, msg: T?) {
        ctx ?: return
        this.handleValueRead(ctx.channel().channelContext, msg)
    }

    final override fun channelActive(ctx: ChannelHandlerContext?) {
        ctx ?: return
        this.handleConnectionEstablished(ctx.channel().channelContext)
    }

    final override fun channelInactive(ctx: ChannelHandlerContext?) {
        ctx ?: return
        this.handleConnectionLoss(ctx.channel().channelContext)
    }

    final override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        ctx ?: return
        this.handleException(ctx.channel().channelContext, cause)
        super.exceptionCaught(ctx, cause)
    }
}