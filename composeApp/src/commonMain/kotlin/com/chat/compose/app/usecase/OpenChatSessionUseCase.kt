package com.chat.compose.app.usecase

import com.application.channel.im.MsgConnectionService
import com.application.channel.im.session.ChatSession
import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/6/20 01:07
 */
class OpenChatSessionUseCase(private val msgConnectionService: MsgConnectionService) {

    fun openChatSession(sessionId: String, sessionType: SessionType): ChatSession {
        return this.msgConnectionService.openSession(sessionId, sessionType)
    }
}