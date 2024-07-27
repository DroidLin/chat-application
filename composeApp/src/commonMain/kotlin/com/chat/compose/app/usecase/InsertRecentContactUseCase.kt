package com.chat.compose.app.usecase

import com.application.channel.im.MsgService
import com.application.channel.message.SessionType
import com.application.channel.message.metadata.RecentContact

/**
 * @author liuzhongao
 * @since 2024/7/26 21:55
 */
class InsertRecentContactUseCase(
    private val msgService: MsgService,
) {

    suspend fun insertRecentContact(sessionId: String, sessionType: SessionType) {
        this.insertRecentContact(RecentContact(sessionId, sessionType))
    }

    suspend fun insertRecentContact(recentContact: RecentContact) {
        this.msgService.insertRecentContact(recentContact)
    }
}