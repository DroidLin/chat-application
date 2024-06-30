package com.app.channel.backend.server.metadata

/**
 * @author liuzhongao
 * @since 2024/6/30 01:29
 */
data class ProfileDTO(
    val userInfo: UserInfoVO? = null,
    val account: AccountVO? = null,
    val sessionInfo: SessionInfoVO? = null
)