package com.application.channel.core.model

import io.netty.channel.Channel
import java.net.SocketAddress

/**
 * @author liuzhongao
 * @since 2024/5/12 21:02
 */
data class ChannelContext(internal val channel: Channel) {
    val channelId: String get() = this.channel.channelId
    val channelLocalAddress: SocketAddress? get() = this.channel.localAddress()
    val channelRemoteAddress: SocketAddress? get() = this.channel.remoteAddress()
}

val Channel.channelContext: ChannelContext
    get() = ChannelContext(channel = this)

val Channel.channelId: String
    get() = this.id().asLongText()