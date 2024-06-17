package com.chat.compose.app.screen.message.vm

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.application.channel.im.MsgConnectionService
import com.application.channel.im.session.ChatSession
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import com.chat.compose.app.metadata.UiMessage
import com.chat.compose.app.metadata.toUiMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import moe.tlaster.precompose.viewmodel.ViewModel
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/6/17 23:55
 */
class SessionDetailViewModel @Inject constructor(
    private val msgConnectionService: MsgConnectionService
) : ViewModel() {

    private var chatSession: ChatSession? = null

    fun openSession(sessionId: String, sessionType: SessionType): Flow<PagingData<UiMessage>> {
        val tmpChatSession = this.chatSession
        if (tmpChatSession != null) {
            this.msgConnectionService.closeSession(tmpChatSession)
        }
        val chatSession = this.msgConnectionService.openSession(sessionId, sessionType)
        try {
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
            ).flow.map { pagingData ->
                pagingData.map(Message::toUiMessage)
            }
        } finally {
            this.chatSession = chatSession
        }
    }
}