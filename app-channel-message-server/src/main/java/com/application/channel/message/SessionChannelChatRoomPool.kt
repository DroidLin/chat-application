package com.application.channel.message

import com.application.channel.message.session.MultiSessionChannel
import com.application.channel.message.session.RoomSessionChannel
import com.application.channel.message.session.impl.ChatRoomSessionChannel
import com.application.channel.message.session.impl.MultiUserSessionChannel
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.concurrent.withLock

/**
 * @author liuzhongao
 * @since 2024/8/6 00:14
 */
object SessionChannelChatRoomPool {

    private val reentrantLock = ReentrantLock()
    private val sessionChannelPool = ConcurrentHashMap<String, RoomSessionChannel>()
    private val logger = Logger.getLogger("SessionChannelChatRoomPool")

    fun newChatRoomSessionChannel(sessionId: String): RoomSessionChannel {
        return ChatRoomSessionChannel(sessionId)
    }

    fun putSessionChannel(sessionChannel: RoomSessionChannel) {
        this.reentrantLock.withLock {
            if (this.sessionChannelPool[sessionChannel.channelInfo.sessionId] == null) {
                this.sessionChannelPool[sessionChannel.channelInfo.sessionId] = sessionChannel
            } else this.logger.log(Level.INFO, "session channel ${sessionChannel.channelInfo.sessionId} already exists.")
        }
    }

    fun getSessionChannel(sessionId: String): RoomSessionChannel? {
        return this.sessionChannelPool[sessionId]
    }

    fun getSessionChannelOrCreate(sessionId: String): RoomSessionChannel {
        if (this.sessionChannelPool[sessionId] == null) {
            this.reentrantLock.withLock {
                if (this.sessionChannelPool[sessionId] == null) {
                    this.sessionChannelPool[sessionId] = this.newChatRoomSessionChannel(sessionId)
                }
            }
        }
        return requireNotNull(sessionChannelPool[sessionId])
    }
}