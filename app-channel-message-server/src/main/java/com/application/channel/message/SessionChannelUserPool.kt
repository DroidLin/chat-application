package com.application.channel.message

import com.application.channel.core.model.ChannelContext
import com.application.channel.message.session.MultiSessionChannel
import com.application.channel.message.session.SessionChannel
import com.application.channel.message.session.impl.MultiUserSessionChannel
import com.application.channel.message.session.impl.SingleUserSessionChannel
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.concurrent.withLock

/**
 * @author liuzhongao
 * @since 2024/8/6 00:16
 */
object SessionChannelUserPool {

    private val reentrantLock = ReentrantLock()
    private val sessionChannelPool = ConcurrentHashMap<String, MultiSessionChannel>()
    private val logger = Logger.getLogger("SessionChannelUserPool")

    internal fun newSingleSessionChannel(sessionId: String, channelContext: ChannelContext): SingleUserSessionChannel {
        return SingleUserSessionChannel(channelContext, sessionId)
    }

    internal fun newMultiSessionChannel(sessionId: String): MultiSessionChannel {
        return MultiUserSessionChannel(sessionId)
    }

    fun putSessionChannel(sessionChannel: MultiSessionChannel) {
        this.reentrantLock.withLock {
            if (this.sessionChannelPool[sessionChannel.channelInfo.sessionId] == null) {
                this.sessionChannelPool[sessionChannel.channelInfo.sessionId] = sessionChannel
            } else this.logger.log(Level.INFO, "session channel ${sessionChannel.channelInfo.sessionId} already exists.")
        }
    }

    fun getSessionChannel(sessionId: String): MultiSessionChannel? {
        return this.sessionChannelPool[sessionId]
    }

    fun getSessionChannelOrCreate(sessionId: String): MultiSessionChannel {
        if (this.sessionChannelPool[sessionId] == null) {
            this.reentrantLock.withLock {
                if (this.sessionChannelPool[sessionId] == null) {
                    this.sessionChannelPool[sessionId] = this.newMultiSessionChannel(sessionId)
                }
            }
        }
        return requireNotNull(sessionChannelPool[sessionId])
    }
}