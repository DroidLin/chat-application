package com.chat.compose.app.usecase

import com.application.channel.im.SingleIMManager
import com.application.channel.message.SessionType
import com.application.channel.message.metadata.RecentContact
import com.application.channel.message.metadata.SessionContact
import com.chat.compose.app.metadata.UiRecentContact
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

class FetchSessionListUseCase {

    val sessionList: Flow<List<UiRecentContact>>
        get() = SingleIMManager.recentContact
            .map { recentContacts -> recentContacts.map(RecentContact::toUiSessionContact) }
            .map { recentContacts -> recentContacts.sortedByDescending { it.timestamp } }
            .distinctUntilChanged()
}