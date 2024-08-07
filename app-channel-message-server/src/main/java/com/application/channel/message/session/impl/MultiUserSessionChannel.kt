package com.application.channel.message.session.impl

import com.application.channel.core.model.ChannelContext
import com.application.channel.message.Callback
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import com.application.channel.message.session.ChannelInfo
import com.application.channel.message.session.MultiSessionChannel
import com.application.channel.message.session.SessionChannel
import java.util.concurrent.locks.ReentrantLock
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.concurrent.withLock

/**
 * @author liuzhongao
 * @since 2024/5/30 22:31
 */
internal class MultiUserSessionChannel(
    private val sessionId: String,
) : SessionChannel, MultiSessionChannel {

    private val reentrantLock = ReentrantLock()
    private val channelList: MutableList<SessionChannel> = ArrayList()
    private val logger = Logger.getLogger("MultiUserSessionChannel")

    val channelContextList: List<ChannelContext>
        get() = this.reentrantLock.withLock {
            this.channelList.mapNotNull { sessionChannel ->
                if (sessionChannel is SingleUserSessionChannel) {
                    sessionChannel.channelContext
                } else {
                    this.logger.log(Level.INFO, "unexpected channel type ${sessionChannel::class.java.name}")
                    null
                }
            }
        }

    override val channelInfo: ChannelInfo = object : ChannelInfo {
        override val sessionId: String get() = this@MultiUserSessionChannel.sessionId
        override val sessionType: SessionType get() = SessionType.P2P
        override val createTime: Long = System.currentTimeMillis()
        override var updateTime: Long = this.createTime
    }

    override val isAlive: Boolean
        get() = this.reentrantLock.withLock {
            this.channelList.any(SessionChannel::isAlive)
        }

    override val isAllAlive: Boolean
        get() = this.reentrantLock.withLock {
            this.channelList.all(SessionChannel::isAlive)
        }

    override fun writeMessage(message: Message, callback: Callback) {
        this.reentrantLock.withLock {
            this.channelList.forEach { sessionChannel ->
                sessionChannel.writeMessage(message, callback)
            }
        }
    }

    override fun addChannel(channel: SessionChannel) {
        this.reentrantLock.withLock {
            if (!this.channelList.contains(channel)) {
                this.channelList += channel
            }
        }
    }

    override fun removeChannel(channel: SessionChannel) {
        this.reentrantLock.withLock {
            if (this.channelList.contains(channel)) {
                this.channelList -= channel
            }
        }
    }
}