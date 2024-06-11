package com.application.channel.im.session

import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/6/10 13:38
 */
internal object ChatSessionFactory {

    @JvmStatic
    fun create(sessionId: String, sessionType: SessionType): ChatSession {
        return when (sessionType) {
            SessionType.P2P -> P2PChatSession(sessionId, sessionType)
            SessionType.Group -> GroupChatSession(sessionId, sessionType)
            else -> throw IllegalArgumentException("illegal session type.")
        }
    }
}