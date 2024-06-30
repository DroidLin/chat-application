package com.app.channel.backend.server.metadata

/**
 * @author liuzhongao
 * @since 2024/6/27 00:58
 */
data class UserInfoVO(
    val userId: Long = -1,
    val userName: String? = null,
    val userEmail: String? = null,
    val userPhone: String? = null
)
