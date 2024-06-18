package com.chat.compose.app.usecase

import com.application.channel.message.SessionType
import com.chat.compose.app.metadata.UiSessionContact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

/**
 * @author liuzhongao
 * @since 2024/6/19 00:14
 */
interface FetchSessionListUseCase {

    val sessionList: Flow<List<UiSessionContact>>
}

class FakeFetchSessionListUseCase constructor() : FetchSessionListUseCase {
    override val sessionList: Flow<List<UiSessionContact>> = flow {
        val data = (0..100).map { index ->
            UiSessionContact(
                sessionId = UUID.randomUUID().toString(),
                sessionType = SessionType.P2P,
                sessionContactName = "张阿斯那",
                unreadCount = (Math.random() * 100).toInt(),
                displayContent = "嗨害嗨哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈",
                time = "2021/03/25 10:22"
            )
        }
        emit(data)
    }
}