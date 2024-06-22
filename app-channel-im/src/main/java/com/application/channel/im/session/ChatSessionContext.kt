package com.application.channel.im.session

import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/6/22 11:50
 */
interface ChatSessionContext {

    val selfUserSessionId: String

    val chatterSessionId: String

    val chatterSessionType: SessionType
}

val ChatSessionCreationContext.toChatSessionContext: ChatSessionContext get() = ChatSessionContext(this)

fun ChatSessionContext(chatSessionCreationContext: ChatSessionCreationContext): ChatSessionContext {
    return ChatSessionContextImpl(
        selfUserSessionId = chatSessionCreationContext.userSessionId,
        chatterSessionId = chatSessionCreationContext.targetSessionId,
        chatterSessionType = chatSessionCreationContext.targetSessionType
    )
}

private data class ChatSessionContextImpl(
    override val selfUserSessionId: String,
    override val chatterSessionId: String,
    override val chatterSessionType: SessionType
) : ChatSessionContext