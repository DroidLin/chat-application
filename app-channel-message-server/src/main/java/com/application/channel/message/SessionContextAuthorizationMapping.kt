package com.application.channel.message

import com.application.channel.core.model.ChannelContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/5/30 23:02
 */
internal class SessionContextAuthorizationMapping @Inject constructor() : AuthorizationMapping {

    private val sessionMapping = ConcurrentHashMap<String, MutableList<ChannelContext>>()

    override fun putAuthContext(sessionId: String, channelContext: ChannelContext) {
        val peersWithSessionId = synchronized(this.sessionMapping) {
            this.sessionMapping.getOrPut(sessionId) { CopyOnWriteArrayList() }
        }
        if (!peersWithSessionId.contains(channelContext)) {
            peersWithSessionId += channelContext
        }
    }

    override fun findSessionId(channelContext: ChannelContext): String? {
        return this.sessionMapping.firstNotNullOfOrNull { (sessionId, channelContextList) ->
            if (channelContextList.contains(channelContext)) {
                sessionId
            } else null
        }
    }

    override fun findChannelContext(sessionId: String): List<ChannelContext> {
        return this.sessionMapping[sessionId]?.toList() ?: emptyList()
    }

    override fun findChannelContext(sessionIdList: List<String>): List<ChannelContext> {
        return sessionIdList.flatMap { sessionId -> this.sessionMapping[sessionId] ?: emptyList() }
    }

    override fun removeAuthContext(sessionId: String) {
        this.sessionMapping.remove(sessionId)
    }

    override fun removeAuthContext(channelContext: ChannelContext) {
        val values = this.sessionMapping.values.toList()
        values.forEach { mutableList ->
            if (mutableList.contains(channelContext)) {
                mutableList -= channelContext
            }
        }
    }

    override fun removeAuthContext(sessionId: String, channelContext: ChannelContext) {
        val channelContextList = this.sessionMapping[sessionId]
        if (channelContextList != null && channelContextList.contains(channelContext)) {
            channelContextList.remove(channelContext)
        }
    }
}