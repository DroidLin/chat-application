package com.application.channel.message.session.impl

import com.application.channel.core.model.ChannelContext
import com.application.channel.message.Callback
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import com.application.channel.message.session.ChannelInfo
import com.application.channel.message.session.RoomSessionChannel
import com.application.channel.message.session.SessionChannel
import com.application.channel.message.session.channelContextList
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * @author liuzhongao
 * @since 2024/8/5 23:21
 */
internal class ChatRoomSessionChannel(
    private val sessionId: String,
) : RoomSessionChannel {

    private val reentrantLock = ReentrantLock()
    private val sessionChannelGroups = LinkedList<SessionChannel>()

    val channelContextList: List<ChannelContext>
        get() = this.reentrantLock.withLock {
            this.sessionChannelGroups.flatMap(SessionChannel::channelContextList)
        }

    override val channelInfo: ChannelInfo = object : ChannelInfo {
        override val sessionId: String get() = this@ChatRoomSessionChannel.sessionId
        override val sessionType: SessionType get() = SessionType.Group
        override val createTime: Long = System.currentTimeMillis()
        override var updateTime: Long = this.createTime
    }

    override val isAlive: Boolean get() = true

    override fun writeMessage(message: Message, callback: Callback) {
        TODO("Not yet implemented")
    }

    override fun addMember(sessionChannel: SessionChannel) {
        this.reentrantLock.withLock {
            val alreadyExist =
                this.sessionChannelGroups.any { it.channelInfo.sessionId == sessionChannel.channelInfo.sessionId }

        }
    }

    override fun removeMember(sessionChannel: SessionChannel) {
        TODO("Not yet implemented")
    }
}