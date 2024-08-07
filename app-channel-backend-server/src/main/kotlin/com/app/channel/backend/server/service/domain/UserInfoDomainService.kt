package com.app.channel.backend.server.service.domain

import com.app.channel.backend.server.CodeConst
import com.app.channel.backend.server.exceptions.BizException
import com.app.channel.backend.server.room.dao.UserInfoDao
import com.app.channel.backend.server.room.metadata.LocalAccount
import com.app.channel.backend.server.room.metadata.LocalUserInfo
import org.springframework.stereotype.Component

/**
 * @author liuzhongao
 * @since 2024/8/2 17:30
 */
@Component
class UserInfoDomainService(
    private val userInfoDao: UserInfoDao,
) {

    suspend fun createUserInfo(userName: String, localAccount: LocalAccount): LocalUserInfo {
        val userInfo = LocalUserInfo(userId = 0, userAccount = localAccount.accountId, userName = userName)
        this.userInfoDao.insertUserInfo(userInfo)
        return this.userInfoDao.fetchUserInfoByAccount(localAccount.accountId)
            ?: throw BizException(code = CodeConst.CODE_INTERNAL_ERROR, message = "server internal error.")
    }
}