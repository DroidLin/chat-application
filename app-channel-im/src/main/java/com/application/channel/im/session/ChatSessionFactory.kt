package com.application.channel.im.session

import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/6/10 13:38
 */
internal object ChatSessionFactory {

    @JvmStatic
    fun create(ctx: ChatSessionCreationContext): ChatSession {
        return when (ctx.targetSessionType) {
            SessionType.P2P -> P2PChatSession(ctx.toChatSessionContext)
            SessionType.Group -> GroupChatSession(ctx.toChatSessionContext)
            else -> throw IllegalArgumentException("illegal session type.")
        }
    }
}