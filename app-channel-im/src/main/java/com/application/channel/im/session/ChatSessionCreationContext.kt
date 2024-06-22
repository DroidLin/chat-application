package com.application.channel.im.session

import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/6/22 11:22
 */
data class ChatSessionCreationContext(
    val userSessionId: String,
    val targetSessionId: String,
    val targetSessionType: SessionType,
)