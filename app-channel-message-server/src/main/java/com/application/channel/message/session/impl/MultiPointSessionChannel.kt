package com.application.channel.message.session.impl

import com.application.channel.message.Callback
import com.application.channel.message.ChatService
import com.application.channel.message.meta.Message
import com.application.channel.message.MultiSessionChannel
import com.application.channel.message.session.ChannelInfo
import com.application.channel.message.session.SessionChannel

/**
 * @author liuzhongao
 * @since 2024/5/30 22:31
 */
class MultiPointSessionChannel(
    private val sessionId: String,
    private val chatService: ChatService,
) : MultiSessionChannel {

    private val channelList: MutableList<SessionChannel> = ArrayList()

    override val anyPointAlive: Boolean
        get() = this.channelList.any { channel ->
            this.chatService.chatServiceController.checkPeerAlive(channel.channelInfo.sessionId)
        }

    override fun addChannel(channel: SessionChannel) {
        synchronized(this.channelList) {
            if (!this.channelList.contains(channel)) {
                this.channelList += channel
            }
        }
    }

    override fun removeChannel(channel: SessionChannel) {
        synchronized(this.channelList) {
            if (this.channelList.contains(channel)) {
                this.channelList -= channel
            }
        }
    }

    override val channelInfo: ChannelInfo = object : ChannelInfo {
        override val sessionId: String get() = this@MultiPointSessionChannel.sessionId
        override val createTime: Long = System.currentTimeMillis()
        override var updateTime: Long = this.createTime
    }

    override fun writeMessage(message: Message, callback: Callback) {
        TODO("Not yet implemented")
    }

}