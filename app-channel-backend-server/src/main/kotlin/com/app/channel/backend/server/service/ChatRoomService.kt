package com.app.channel.backend.server.service

import com.app.channel.backend.server.CodeConst
import com.app.channel.backend.server.exceptions.BizException
import com.app.channel.backend.server.metadata.ChatRoomInfoVO
import com.app.channel.backend.server.room.AppBackendDatabase
import com.app.channel.backend.server.service.domain.AccountDomainService
import com.app.channel.backend.server.service.domain.ChatRoomDomainService
import com.app.channel.backend.server.service.domain.SessionAccountDomainService
import com.app.channel.backend.server.service.domain.UserInfoDomainService
import com.application.channel.message.SessionType
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author liuzhongao
 * @since 2024/8/2 17:14
 */
@Component
class ChatRoomService(
    private val appBackendDatabase: AppBackendDatabase,
    private val userInfoDomainService: UserInfoDomainService,
    private val accountDomainService: AccountDomainService,
    private val sessionAccountDomainService: SessionAccountDomainService,
    private val chatRoomDomainService: ChatRoomDomainService,
    private val basicUserService: BasicUserService,
    private val userService: UserService,
) {

    suspend fun fetchChatRoomInfo(sessionId: String): ChatRoomInfoVO? = coroutineScope {
        val localChatRoomInfo = this@ChatRoomService.chatRoomDomainService.fetchChatRoomInfo(sessionId, SessionType.Group)
                ?: return@coroutineScope null

        val creatorDeferred = async {
            this@ChatRoomService.userService.getUserInfoBySessionId(localChatRoomInfo.creatorSessionId)
        }
        val chatRoomMemberDeferred = async {
            localChatRoomInfo.members.split(",")
                .mapNotNull { sessionId -> this@ChatRoomService.userService.getUserInfoBySessionId(sessionId) }
        }
        val chatRoomManagerDeferred = async {
            localChatRoomInfo.memberManager.split(",")
                .mapNotNull { sessionId -> this@ChatRoomService.userService.getUserInfoBySessionId(sessionId) }
        }

        val creator = creatorDeferred.await() ?: return@coroutineScope null
        val chatRoomMember = chatRoomMemberDeferred.await()
        val chatRoomManager = chatRoomManagerDeferred.await()

        ChatRoomInfoVO(
            sessionId = localChatRoomInfo.sessionId,
            sessionTypeCode = localChatRoomInfo.sessionTypeCode,
            creator = creator,
            chatRoomMember = chatRoomMember,
            chatRoomManager = chatRoomManager
        )
    }

    suspend fun createChatRoom(
        chatRoomName: String,
        creatorSessionId: String,
        creatorSessionTypeCode: Int
    ): ChatRoomInfoVO {
        return this.appBackendDatabase.withTransaction(false) {
            val sessionId = UUID.randomUUID().toString()
            val localAccount = this@ChatRoomService.accountDomainService.createAccount(sessionId, sessionId)
            val localUserInfo = this@ChatRoomService.userInfoDomainService.createUserInfo(chatRoomName, localAccount)
            val localSessionInfo = this@ChatRoomService.sessionAccountDomainService.createSessionInfo(
                sessionId = sessionId,
                sessionType = SessionType.Group,
                localAccount = localAccount,
                localUserInfo = localUserInfo
            )
            this@ChatRoomService.chatRoomDomainService.createChatRoom(
                localSessionInfo = localSessionInfo,
                localAccount = localAccount,
                creatorSessionId = creatorSessionId,
                creatorSessionTypeCode = creatorSessionTypeCode
            )
            this@ChatRoomService.fetchChatRoomInfo(sessionId)
                ?: throw BizException(code = CodeConst.CODE_INTERNAL_ERROR, message = "create chat room failure.")
        }
    }
}