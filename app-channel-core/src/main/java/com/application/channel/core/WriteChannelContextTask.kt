package com.application.channel.core

import com.application.channel.core.model.ChannelContext
import com.application.channel.core.model.Writable
import com.application.channel.core.model.channelContext
import io.netty.channel.Channel
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.ChannelGroupFutureListener
import io.netty.channel.group.ChannelMatcher

/**
 * @author liuzhongao
 * @since 2024/8/5 23:54
 */
class WriteChannelContextTask(
    private val channelGroup: ChannelGroup,
    private val channelContextList: List<ChannelContext>,
    private val value: Writable,
    private val listener: Listener?
) : Runnable {
    override fun run() {
        val anyChannelConnected = this.channelGroup.filter { channel ->
            channel != null && (channel.channelContext in this.channelContextList)
        }.any { it.isActive }
        if (!anyChannelConnected) {
            this.listener?.onFailure(NotConnectedException("none of any channel is connected."))
            return
        }
        val channelGroupFutureListener = ChannelGroupFutureListener { future ->
            if (future.isSuccess || future.isPartialSuccess) {
                listener?.onSuccess()
            } else listener?.onFailure(future.cause() ?: Throwable())
        }
        this.channelGroup.writeAndFlush(this.value, object : ChannelMatcher {
            override fun matches(channel: Channel?): Boolean {
                channel ?: return false
                return channel.channelContext in this@WriteChannelContextTask.channelContextList
            }
        }).addListener(channelGroupFutureListener)
    }
}