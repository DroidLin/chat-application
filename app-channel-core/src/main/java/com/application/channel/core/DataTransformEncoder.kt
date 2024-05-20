package com.application.channel.core

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import io.netty.handler.codec.MessageToMessageEncoder
import java.net.SocketAddress

/**
 * @author liuzhongao
 * @since 2024/5/20 23:19
 */
abstract class DataTransformEncoder<In : Any?, Out: Any?> : MessageToMessageEncoder<In>() {
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

    final override fun bind(ctx: ChannelHandlerContext?, localAddress: SocketAddress?, promise: ChannelPromise?) {
        super.bind(ctx, localAddress, promise)
    }

    final override fun connect(
        ctx: ChannelHandlerContext?,
        remoteAddress: SocketAddress?,
        localAddress: SocketAddress?,
        promise: ChannelPromise?
    ) {
        super.connect(ctx, remoteAddress, localAddress, promise)
    }

    final override fun disconnect(ctx: ChannelHandlerContext?, promise: ChannelPromise?) {
        super.disconnect(ctx, promise)
    }

    final override fun close(ctx: ChannelHandlerContext?, promise: ChannelPromise?) {
        super.close(ctx, promise)
    }

    final override fun deregister(ctx: ChannelHandlerContext?, promise: ChannelPromise?) {
        super.deregister(ctx, promise)
    }

    final override fun read(ctx: ChannelHandlerContext?) {
        super.read(ctx)
    }

    final override fun write(ctx: ChannelHandlerContext?, msg: Any?, promise: ChannelPromise?) {
        super.write(ctx, msg, promise)
    }

    final override fun flush(ctx: ChannelHandlerContext?) {
        super.flush(ctx)
    }

    final override fun acceptOutboundMessage(msg: Any?): Boolean {
        return super.acceptOutboundMessage(msg)
    }

    final override fun encode(ctx: ChannelHandlerContext?, msg: In, out: MutableList<Any>?) {
        ctx ?: return
        msg ?: return
        out ?: return
        this.encode(msg, out as MutableList<Out>)
    }

    abstract fun encode(msg: In, out: MutableList<Out>)
}