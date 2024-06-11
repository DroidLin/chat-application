package com.application.channel.message.session.impl

import com.application.channel.message.Callback
import com.application.channel.message.ChatService
import com.application.channel.message.session.ChannelInfo
import com.application.channel.message.meta.Message
import com.application.channel.message.session.SessionChannel

/**
 * @author liuzhongao
 * @since 2024/5/30 00:22
 */
class SinglePointSessionChannel(
    private val sessionId: String,
    private val chatService: ChatService
) : SessionChannel {

    override val channelInfo: ChannelInfo = object : ChannelInfo {
        override val sessionId: String get() = this@SinglePointSessionChannel.sessionId
        override val createTime: Long = System.currentTimeMillis()
        override val updateTime: Long = this.createTime
    }

    override fun writeMessage(message: Message, callback: Callback) {
        TODO("Not yet implemented")
    }
}