package com.chat.compose.app.usecase

import com.application.channel.im.MsgService
import com.application.channel.message.SessionType
import com.application.channel.message.metadata.SessionContact

/**
 * @author liuzhongao
 * @since 2024/7/26 21:53
 */
class InsertSessionContactUseCase(
    private val msgService: MsgService,
) {

    suspend fun insertSessionContact(sessionId: String, sessionType: SessionType) {
        this.insertSessionContact(SessionContact(sessionId, sessionType))
    }

    suspend fun insertSessionContact(sessionContact: SessionContact) {
        this.msgService.insertSessionContact(sessionContact)
    }
}