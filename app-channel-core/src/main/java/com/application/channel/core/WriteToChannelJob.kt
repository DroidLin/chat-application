package com.application.channel.core

import com.application.channel.core.model.channelContext
import io.netty.channel.Channel
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.ChannelGroupFuture
import io.netty.channel.group.ChannelGroupFutureListener
import io.netty.channel.group.ChannelMatcher

/**
 * @author liuzhongao
 * @since 2024/5/18 16:13
 */
class WriteToChannelJob(
    private val channelGroup: ChannelGroup,
    private val value: Any?,
    private val channelMatcher: ChannelContextMatcher,
    private val listener: Listener?
) : Runnable {

    override fun run() {
        val anyChannelConnected = this.channelGroup.filter { channel ->
            channel != null && this.channelMatcher.match(channel.channelContext)
        }.any { it.isActive }
        if (!anyChannelConnected) {
            this.listener?.onFailure(NotConnectedException("none of any channel is connected."))
            return
        }
        val channelGroupFutureListener = object : ChannelGroupFutureListener {
            override fun operationComplete(future: ChannelGroupFuture?) {
                if (future == null) return
                if (future.isSuccess || future.isPartialSuccess) {
                    listener?.onSuccess()
                } else listener?.onFailure(future.cause() ?: Throwable())
            }
        }
        this.channelGroup.writeAndFlush(value, object : ChannelMatcher {
            override fun matches(channel: Channel?): Boolean {
                channel ?: return false
                val channelContext = channel.channelContext
                return this@WriteToChannelJob.channelMatcher.match(channelContext)
            }
        }).addListener(channelGroupFutureListener)
    }
}