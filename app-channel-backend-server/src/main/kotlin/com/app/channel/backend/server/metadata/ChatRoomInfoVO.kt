package com.app.channel.backend.server.metadata

/**
 * @author liuzhongao
 * @since 2024/8/3 09:44
 */
data class ChatRoomInfoVO(
    val sessionId: String,
    val sessionTypeCode: Int,
    val creator: ProfileDTO,
    val chatRoomMember: List<ProfileDTO>,
    val chatRoomManager: List<ProfileDTO>
)
