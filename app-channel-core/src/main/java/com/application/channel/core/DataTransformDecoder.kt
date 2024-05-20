package com.application.channel.core

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder

/**
 * @author liuzhongao
 * @since 2024/5/20 23:25
 */
abstract class DataTransformDecoder<In : Any?, Out : Any?> : MessageToMessageDecoder<In>() {

    final override fun handlerAdded(ctx: ChannelHandlerContext?) {
        super.handlerAdded(ctx)
    }

    final override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        super.handlerRemoved(ctx)
    }

    final override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        super.exceptionCaught(ctx, cause)
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

    final override fun channelActive(ctx: ChannelHandlerContext?) {
        super.channelActive(ctx)
    }

    final override fun channelInactive(ctx: ChannelHandlerContext?) {
        super.channelInactive(ctx)
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

    final override fun decode(ctx: ChannelHandlerContext?, msg: In, out: MutableList<Any>?) {
        ctx ?: return
        msg ?: return
        out ?: return
        this.decode(msg, out as MutableList<Out>)
    }

    abstract fun decode(msg: In, out: MutableList<Out>)
}