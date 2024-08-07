package com.app.channel.backend.server.metadata

/**
 * @author liuzhongao
 * @since 2024/8/2 17:17
 */
data class ChatRoomInfoDTO(
    val userInfo: UserInfoVO? = null,
    val account: AccountVO? = null,
    val sessionInfo: SessionInfoVO? = null,
    val chatRoomInfo: ChatRoomInfoVO? = null,
)
