package com.application.channel.core.client

import com.application.channel.core.model.channelContext
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.SimpleChannelInboundHandler

/**
 * @author liuzhongao
 * @since 2024/6/23 03:31
 */
internal class TcpChannelActivityAdapter(private val observer: TcpConnectionObserver?) : ChannelInboundHandlerAdapter()  {

    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        this.observer?.onConnectionLost(ctx.channel().channelContext, null)
    }
}