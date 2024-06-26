package com.app.channel.backend.server.metadata

/**
 * @author liuzhongao
 * @since 2024/6/27 00:58
 */
data class UserInfoVO(
    val userId: Long,
    val userName: String,
    val sessionId: String,
    val userEmail: String? = null,
    val userPhone: String? = null
)
