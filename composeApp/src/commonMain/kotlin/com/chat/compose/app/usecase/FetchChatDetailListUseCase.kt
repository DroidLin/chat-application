package com.chat.compose.app.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.application.channel.im.session.ChatSession
import com.application.channel.message.SessionType
import com.chat.compose.app.metadata.UiMessageItem
import com.chat.compose.app.metadata.toUiMessage
import com.chat.compose.app.metadata.toUiSessionContact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * @author liuzhongao
 * @since 2024/6/22 12:08
 */
class FetchChatDetailListUseCase(private val fetchSessionContactUseCase: FetchSessionContactUseCase) {

    fun openPagerListFlow(chatSession: ChatSession): Flow<PagingData<UiMessageItem>> {
        val chatSessionContext = chatSession.context
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                chatSession.historyMessageSource(anchorMessage = null, limit = 20)
            }
        )
            .flow
            .map { pagingData ->
                val selfUserSessionContact = this.fetchSessionContactUseCase.fetchSessionContact(
                    sessionId = chatSessionContext.selfUserSessionId,
                    sessionType = SessionType.P2P
                )?.toUiSessionContact()
                val chatterSessionContact = this.fetchSessionContactUseCase.fetchSessionContact(
                    sessionId = chatSessionContext.chatterSessionId,
                    sessionType = chatSessionContext.chatterSessionType
                )?.toUiSessionContact()
                pagingData.map { message ->

                    val isSenderMessage = message.sender.sessionId == chatSession.context.selfUserSessionId
                    val isReceiverMessage = message.sender.sessionId == chatSession.context.chatterSessionId || message.receiver.sessionId == chatSession.context.chatterSessionId

                    val uiSessionContact = when {
                        isSenderMessage && isReceiverMessage -> selfUserSessionContact ?: chatterSessionContact
                        isSenderMessage -> selfUserSessionContact
                        isReceiverMessage -> chatterSessionContact
                        else -> null
                    }

                    UiMessageItem(
                        uiSessionContact = uiSessionContact,
                        uiMessage = message.toUiMessage(
                            isSenderMessage = isSenderMessage,
                            isReceiverMessage = isReceiverMessage,
                        )
                    )
                }
            }
            .distinctUntilChanged()
    }
}