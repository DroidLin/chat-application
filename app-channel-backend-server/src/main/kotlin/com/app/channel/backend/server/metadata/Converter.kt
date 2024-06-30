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

fun LocalSessionInfo.toSessionInfoVO(): SessionInfoVO {
    return SessionInfoVO(
        sessionId = this.sessionId,
        accountId = this.accountId,
        userId = this.userId,
        sessionTypeCode = this.sessionTypeCode
    )
}

fun LocalUserInfo.toUserInfoVO(): UserInfoVO {
    return UserInfoVO(
        userId = this.userId,
        userPhone = this.userPhone,
        userEmail = this.userEmail,
        userName = this.userName
    )
}
