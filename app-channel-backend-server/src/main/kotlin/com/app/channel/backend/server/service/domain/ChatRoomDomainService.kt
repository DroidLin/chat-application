package com.app.channel.backend.server.service.domain

import com.app.channel.backend.server.CodeConst
import com.app.channel.backend.server.exceptions.BizException
import com.app.channel.backend.server.room.dao.ChatRoomDao
import com.app.channel.backend.server.room.metadata.LocalAccount
import com.app.channel.backend.server.room.metadata.LocalChatRoomInfo
import com.app.channel.backend.server.room.metadata.LocalSessionInfo
import com.application.channel.message.SessionType
import org.springframework.stereotype.Component

/**
 * @author liuzhongao
 * @since 2024/8/2 17:36
 */
@Component
class ChatRoomDomainService(
    private val chatRoomDao: ChatRoomDao,
) {

    suspend fun fetchChatRoomInfo(sessionId: String, sessionType: SessionType): LocalChatRoomInfo? {
        return this.chatRoomDao.fetchChatRoomInfo(sessionId, sessionType.value)
    }

    suspend fun createChatRoom(
        localSessionInfo: LocalSessionInfo,
        localAccount: LocalAccount,
        creatorSessionId: String,
        creatorSessionTypeCode: Int
    ): LocalChatRoomInfo {
        // creator is regard as member and creator by by default.
        val memberAndManager = listOf(creatorSessionId).joinToString(",")
        val localChatRoomInfo = LocalChatRoomInfo(
            sessionId = localSessionInfo.sessionId,
            sessionTypeCode = localSessionInfo.sessionTypeCode,
            accountId = localAccount.accountId,
            createTimeStamp = System.currentTimeMillis(),
            updateTimeStamp = System.currentTimeMillis(),
            creatorSessionId = creatorSessionId,
            creatorSessionTypeCode = creatorSessionTypeCode,
            members = memberAndManager,
            memberManager = memberAndManager
        )
        this.chatRoomDao.insert(localChatRoomInfo)
        return this.chatRoomDao.fetchChatRoomInfo(sessionId = localChatRoomInfo.sessionId, sessionTypeCode = localChatRoomInfo.sessionTypeCode)
            ?: throw BizException(code = CodeConst.CODE_INTERNAL_ERROR, message = "server internal error.")
    }
}