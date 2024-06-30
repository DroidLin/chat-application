package com.chat.compose.app.usecase

import com.application.channel.im.MsgService
import com.application.channel.im.SessionExtensions
import com.chat.compose.app.metadata.Profile

/**
 * @author liuzhongao
 * @since 2024/6/30 01:23
 */
class UpdateSessionContactUserBasicInfoUseCase(private val msgService: MsgService) {

    suspend fun updateSessionContactUserInfo(userInfoList: List<Profile>) {
        this.msgService.withTransaction(readOnly = false) { msgService ->
            userInfoList.forEach { profile ->
                val sessionInfo = profile.sessionInfo ?: return@forEach
                val sessionId = sessionInfo.sessionId ?: return@forEach
                val sessionType = sessionInfo.sessionType
                msgService.updateSessionContactExtensions(sessionId, sessionType) {
                    this[SessionExtensions.KEY_USER_INFO_NAME] = profile.userInfo?.userName
                    this[SessionExtensions.KEY_USER_INFO_ID] = profile.userInfo?.userId
                }
            }
        }
    }
}