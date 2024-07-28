package com.application.channel.core

import com.application.channel.core.model.ChannelContext
import com.application.channel.core.model.channelContext
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

/**
 * @author liuzhongao
 * @since 2024/6/23 11:48
 */
@Sharable
class DataReader(
    private val onReceive: (ChannelContext, Any?) -> Unit,
) : SimpleChannelInboundHandler<Any>() {
    override fun channelRead0(ctx: ChannelHandlerContext?, msg: Any?) {
        if (ctx == null || msg == null) return
        this.onReceive(ctx.channel().channelContext, msg)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        super.exceptionCaught(ctx, cause)
        cause?.printStackTrace()
    }
}