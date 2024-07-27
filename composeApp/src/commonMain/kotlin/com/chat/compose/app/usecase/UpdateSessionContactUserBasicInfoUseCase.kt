package com.chat.compose.app.usecase

import com.application.channel.im.MsgService
import com.application.channel.im.userInfoString
import com.chat.compose.app.metadata.Profile
import com.chat.compose.app.util.toJson

/**
 * @author liuzhongao
 * @since 2024/6/30 01:23
 */
class UpdateSessionContactUserBasicInfoUseCase(private val msgService: MsgService) {

    suspend fun updateRecentContactProfile(userInfoList: List<Profile>) {
        this.msgService.withTransaction(readOnly = false) { msgService ->
            userInfoList.forEach { profile ->
                val sessionInfo = profile.sessionInfo ?: return@forEach
                val sessionId = sessionInfo.sessionId ?: return@forEach
                val sessionType = sessionInfo.sessionType
                val profileJSONString = profile.toJson()
                msgService.updateRecentContact(sessionId, sessionType) {
                    this.userInfoString = profileJSONString
                }
            }
        }
    }

    suspend fun updateSessionContactProfile(userInfoList: List<Profile>) {
        this.msgService.withTransaction(readOnly = false) { msgService ->
            userInfoList.forEach { profile ->
                val sessionInfo = profile.sessionInfo ?: return@forEach
                val sessionId = sessionInfo.sessionId ?: return@forEach
                val sessionType = sessionInfo.sessionType
                val profileJSONString = profile.toJson()
                msgService.updateSessionContact(sessionId, sessionType) {
                    this.userInfoString = profileJSONString
                }
            }
        }
    }
}