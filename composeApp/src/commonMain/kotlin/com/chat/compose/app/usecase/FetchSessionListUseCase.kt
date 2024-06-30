package com.chat.compose.app.usecase

import com.application.channel.im.MsgService
import com.application.channel.im.SingleIMManager
import com.application.channel.message.SessionType
import com.application.channel.message.metadata.SessionContact
import com.chat.compose.app.metadata.UiSessionContact
import com.chat.compose.app.metadata.toUiSessionContact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

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

class FetchSessionListUseCase(private val msgService: MsgService) {

    val sessionList: Flow<List<UiSessionContact>>
        get() = SingleIMManager.sessionContact
            .map { sessionContactList -> sessionContactList.map(SessionContact::toUiSessionContact) }
            .distinctUntilChanged()
}