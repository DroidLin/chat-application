package com.app.channel.backend.server.metadata

/**
 * @author liuzhongao
 * @since 2024/6/30 01:26
 */
data class SessionInfoVO(
    val userId: Long = -1,
    val accountId: String? = null,
    val sessionId: String? = null,
    val sessionTypeCode: Int = -1,
)