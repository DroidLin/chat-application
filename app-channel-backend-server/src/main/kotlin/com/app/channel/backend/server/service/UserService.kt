package com.app.channel.backend.server.service

import com.app.channel.backend.server.metadata.ProfileDTO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Component

/**
 * @author liuzhongao
 * @since 2024/6/29 12:44
 */
@Component
class UserService(
    private val basicUserService: BasicUserService,
) {

    suspend fun getUserInfo(userId: Long): ProfileDTO? {
        val userInfo = this.basicUserService.fetchUserInfoById(userId)
        return userInfo
    }

    suspend fun getUserInfoBySessionId(sessionId: String): ProfileDTO? {
        return this.basicUserService.fetchUserInfoBySessionId(sessionId)
    }

    suspend fun getUserInfo(ids: List<String>): List<ProfileDTO> {
        return coroutineScope {
            ids.map { userId ->
                async {
                    this@UserService.getUserInfo(userId.toLong())
                }
            }.awaitAll().filterNotNull()
        }
    }

    suspend fun getUserInfoBySessionId(sessionIdList: List<String>): List<ProfileDTO> {
        return coroutineScope {
            sessionIdList.map { sessionId ->
                async {
                    this@UserService.getUserInfoBySessionId(sessionId)
                }
            }.awaitAll().filterNotNull()
        }
    }
}