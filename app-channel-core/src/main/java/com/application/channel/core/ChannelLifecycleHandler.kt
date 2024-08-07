package com.application.channel.core

import com.application.channel.core.model.channelContext
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext

/**
 * @author liuzhongao
 * @since 2024/8/6 01:05
 */
@Sharable
class ChannelLifecycleHandler(
    private val channelLifecycleObserver: ChannelLifecycleObserver
): ChannelHandlerAdapter() {

    override fun handlerAdded(ctx: ChannelHandlerContext?) {
        val channelContext = ctx?.channel()?.channelContext ?: return
        this.channelLifecycleObserver.onChannelAttached(channelContext)
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        val channelContext = ctx?.channel()?.channelContext ?: return
        this.channelLifecycleObserver.onChannelDetached(channelContext)
    }
}