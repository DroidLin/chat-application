package com.app.channel.backend.server.service

import com.app.channel.backend.server.CodeConst
import com.app.channel.backend.server.exceptions.BizException
import com.app.channel.backend.server.metadata.*
import com.app.channel.backend.server.room.AppBackendDatabase
import com.app.channel.backend.server.room.dao.AccountDao
import com.app.channel.backend.server.room.dao.SessionInfoDao
import com.app.channel.backend.server.room.dao.UserInfoDao
import com.app.channel.backend.server.room.metadata.LocalAccount
import com.app.channel.backend.server.room.metadata.LocalSessionInfo
import com.app.channel.backend.server.room.metadata.LocalUserInfo
import com.application.channel.message.SessionType
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author liuzhongao
 * @since 2024/6/28 01:17
 */
@Component
class ApiUserService(
    private val appBackendDatabase: AppBackendDatabase,
    private val userInfoDao: UserInfoDao,
    private val accountDao: AccountDao,
    private val sessionInfoDao: SessionInfoDao,
) {

    suspend fun checkUserAccountExist(userAccount: String?): Boolean {
        if (userAccount.isNullOrBlank()) {
            throw BizException(code = CodeConst.CODE_INVALID_PARAMETER, message = "invalid parameter.")
        }
        return this.accountDao.checkCountOfUserAccount(userAccount) > 0
    }

    suspend fun fetchUserInfoById(userId: Long): ProfileDTO? {
        val userInfo = this.userInfoDao.fetchUserInfo(userId) ?: return null
        val account = this.accountDao.fetchAccountById(userInfo.userAccount) ?: return null
        val sessionInfo = this.sessionInfoDao.fetchSessionInfo(userInfo.userAccount) ?: return null
        return ProfileDTO(
            userInfo = userInfo.toUserInfoVO(),
            account = account.toAccountVO(),
            sessionInfo = sessionInfo.toSessionInfoVO()
        )
    }

    suspend fun fetchUserInfoBySessionId(sessionId: String): ProfileDTO? {
        val sessionInfo = this.sessionInfoDao.fetchSessionInfoBySessionId(sessionId) ?: return null
        val userInfo = this.userInfoDao.fetchUserInfo(sessionInfo.userId) ?: return null
        val account = this.accountDao.fetchAccountById(sessionInfo.accountId) ?: return null
        return ProfileDTO(
            userInfo = userInfo.toUserInfoVO(),
            account = account.toAccountVO(),
            sessionInfo = sessionInfo.toSessionInfoVO()
        )
    }

    suspend fun checkAndLogin(userAccount: String?, passwordHash: String?): ProfileDTO {
        if (userAccount.isNullOrEmpty() || passwordHash.isNullOrEmpty()) {
            throw BizException(code = CodeConst.CODE_INVALID_PARAMETER, message = "invalid parameters.")
        }
        val account = this.accountDao.fetchAccountById(userAccount = userAccount)
            ?: throw BizException(code = CodeConst.CODE_USER_NOT_FOUND, message = "user not found.")
        if (account.passwordHash != passwordHash) {
            throw BizException(code = CodeConst.CODE_PASSWORD_ERROR_OR_USER_NOT_EXIST, message = "incorrect password or user does not exist.")
        }
        val userInfo = this.userInfoDao.fetchUserInfoByAccount(userAccount = userAccount)
            ?: throw BizException(code = CodeConst.CODE_INTERNAL_ERROR, message = "internal error.")
        val sessionInfo = this.sessionInfoDao.fetchSessionInfo(userAccount = userAccount)
            ?: throw BizException(code = CodeConst.CODE_INTERNAL_ERROR, message = "internal error")
        return ProfileDTO(
            userInfo = userInfo.toUserInfoVO(),
            account = account.toAccountVO(),
            sessionInfo = sessionInfo.toSessionInfoVO()
        )
    }

    suspend fun createUserAccount(userName: String?, userAccount: String?, passwordHash: String?): ProfileDTO? {
        if (userName.isNullOrEmpty() || passwordHash.isNullOrEmpty() || userAccount.isNullOrEmpty()) {
            throw BizException(code = CodeConst.CODE_INVALID_PARAMETER, message = "invalid parameters.")
        }
        if (this.accountDao.fetchAccountById(userAccount) != null) {
            throw BizException(code = CodeConst.CODE_USER_ALREADY_EXIST, message = "user already exists.")
        }
        return this.appBackendDatabase.withTransaction(false) {
            val account = LocalAccount(
                accountId = userAccount,
                createTime = System.currentTimeMillis(),
                updateTime = System.currentTimeMillis(),
                passwordHash = passwordHash
            )
            this@ApiUserService.accountDao.insertAccount(account)
            this@ApiUserService.userInfoDao.insertUserInfo(LocalUserInfo(userId = 0, userAccount = userAccount, userName = userName))
            val userInfo = this@ApiUserService.userInfoDao.fetchUserInfoByAccount(account.accountId)
                ?: throw BizException(code = CodeConst.CODE_INTERNAL_ERROR, message = "server internal error.")
            val sessionInfo = LocalSessionInfo(UUID.randomUUID().toString(), userInfo.userId, account.accountId, SessionType.P2P.value)
            this@ApiUserService.sessionInfoDao.insertSessionInfo(sessionInfo)
            ProfileDTO(
                userInfo = userInfo.toUserInfoVO(),
                account = account.toAccountVO(),
                sessionInfo = sessionInfo.toSessionInfoVO()
            )
        }
    }
}