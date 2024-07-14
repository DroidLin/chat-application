package com.app.channel.backend.server.service

import com.app.channel.backend.server.metadata.ProfileDTO
import com.app.channel.backend.server.metadata.toAccountVO
import com.app.channel.backend.server.metadata.toSessionInfoVO
import com.app.channel.backend.server.metadata.toUserInfoVO
import com.app.channel.backend.server.room.dao.AccountDao
import com.app.channel.backend.server.room.dao.SessionInfoDao
import com.app.channel.backend.server.room.dao.UserInfoDao
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Component

/**
 * @author liuzhongao
 * @since 2024/7/2 00:18
 */
@Component
class SearchService(
    private val userInfoDao: UserInfoDao,
    private val accountDao: AccountDao,
    private val sessionInfoDao: SessionInfoDao,
) {

    suspend fun searchComplex(query: String?): Map<String, Any> = coroutineScope {
        val searchUserInfoLocalList = this@SearchService.userInfoDao.searchMatch("%$query%", 10).map { userInfo ->
            async {
                val accountDeferred = async {
                    this@SearchService.accountDao.fetchAccountById(userInfo.userAccount)
                }
                val sessionInfoDeferred = async {
                    this@SearchService.sessionInfoDao.fetchSessionInfo(userInfo.userAccount)
                }
                val account = accountDeferred.await()
                val sessionInfo = sessionInfoDeferred.await()
                if (account == null || sessionInfo == null) return@async null
                ProfileDTO(
                    userInfo = userInfo.toUserInfoVO(),
                    account = account.toAccountVO(),
                    sessionInfo = sessionInfo.toSessionInfoVO()
                )
            }
        }
        mapOf("userInfo" to searchUserInfoLocalList.awaitAll())
    }
}