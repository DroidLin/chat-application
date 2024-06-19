package com.chat.compose.app.usecase

import com.application.channel.message.SessionType
import com.application.channel.message.metadata.SessionContact
import com.chat.compose.app.metadata.UiSessionContact
import kotlinx.coroutines.flow.Flow

/**
 * @author liuzhongao
 * @since 2024/6/19 00:14
 */
val sessionContactV1 = SessionContact(
    sessionId = "liuzhongao",
    sessionType = SessionType.P2P,
)
val sessionContactV2 = SessionContact(
    sessionId = "tenderlove",
    sessionType = SessionType.P2P,
)

interface FetchSessionListUseCase {

    val sessionList: Flow<List<UiSessionContact>>
}