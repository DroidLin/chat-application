package com.app.channel.backend.server.metadata

import com.app.channel.backend.server.room.metadata.LocalAccount
import com.app.channel.backend.server.room.metadata.LocalSessionInfo
import com.app.channel.backend.server.room.metadata.LocalUserInfo

/**
 * @author liuzhongao
 * @since 2024/6/27 00:59
 */
fun LocalAccount.toAccountVO(): AccountVO {
    return AccountVO(
        accountId = this.accountId,
        createTime = this.createTime,
        updateTime = this.updateTime
    )
}

fun LocalUserInfo.toUserInfoVO(localSessionInfo: LocalSessionInfo): UserInfoVO {
    return UserInfoVO(
        sessionId = localSessionInfo.sessionId,
        userId = this.userId,
        userPhone = this.userPhone,
        userEmail = this.userEmail,
        userName = this. userName
    )
}