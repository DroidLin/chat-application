package com.chat.compose.app.usecase

import com.application.channel.im.MsgService
import com.application.channel.message.SessionType
import com.application.channel.message.metadata.SessionContact
import kotlinx.coroutines.flow.Flow

/**
 * @author liuzhongao
 * @since 2024/6/20 21:43
 */
class FetchSessionContactUseCase(private val msgService: MsgService) {

    suspend fun fetchSessionContact(sessionId: String, sessionType: SessionType): SessionContact? {
        return this.msgService.fetchSessionContact(sessionId, sessionType)
    }

    fun fetchObservableSessionContact(sessionId: String, sessionType: SessionType): Flow<SessionContact?> {
        return this.msgService.fetchObservableSessionContact(sessionId, sessionType)
    }
}