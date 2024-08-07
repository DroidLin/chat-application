package com.application.channel.message.session.impl

import com.application.channel.core.model.ChannelContext
import com.application.channel.message.Callback
import com.application.channel.message.ChatService
import com.application.channel.message.SessionType
import com.application.channel.message.session.ChannelInfo
import com.application.channel.message.meta.Message
import com.application.channel.message.session.SessionChannel

/**
 * @author liuzhongao
 * @since 2024/5/30 00:22
 */
internal class SingleUserSessionChannel(
    val channelContext: ChannelContext,
    private val sessionId: String,
) : SessionChannel {

    override val channelInfo: ChannelInfo = object : ChannelInfo {
        override val sessionId: String get() = this@SingleUserSessionChannel.sessionId
        override val sessionType: SessionType get() = SessionType.P2P
        override val createTime: Long = System.currentTimeMillis()
        override val updateTime: Long = this.createTime
    }

    override val isAlive: Boolean get() = this.channelContext.isChannelActive

    override fun writeMessage(message: Message, callback: Callback) {
        TODO("Not yet implemented")
    }
}