package com.app.channel.backend.server.service.domain

import com.app.channel.backend.server.CodeConst
import com.app.channel.backend.server.exceptions.BizException
import com.app.channel.backend.server.room.dao.SessionInfoDao
import com.app.channel.backend.server.room.metadata.LocalAccount
import com.app.channel.backend.server.room.metadata.LocalSessionInfo
import com.app.channel.backend.server.room.metadata.LocalUserInfo
import com.application.channel.message.SessionType
import org.springframework.stereotype.Component

/**
 * @author liuzhongao
 * @since 2024/8/2 17:33
 */
@Component
class SessionAccountDomainService(
    private val sessionInfoDao: SessionInfoDao,
) {

    suspend fun createSessionInfo(
        sessionId: String,
        sessionType: SessionType,
        localAccount: LocalAccount,
        localUserInfo: LocalUserInfo,
    ): LocalSessionInfo {
        val sessionInfo =
            LocalSessionInfo(sessionId, localUserInfo.userId, localAccount.accountId, SessionType.P2P.value)
        this.sessionInfoDao.insertSessionInfo(sessionInfo)
        return this.sessionInfoDao.fetchSessionInfoBySessionId(sessionId)
            ?: throw BizException(code = CodeConst.CODE_INTERNAL_ERROR, message = "server internal error.")
    }
}