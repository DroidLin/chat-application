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
import kotlinx.coroutines.flow.*
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
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
        this.closeSession()
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
                .distinctUntilChanged()
                .stateIn(this.viewModelScope, SharingStarted.Lazily, PagingData.empty())
        } finally {
            this.chatSession = chatSession
        }
    }

    fun closeSession() {
        val tmpChatSession = this.chatSession
        if (tmpChatSession != null) {
            this.msgConnectionService.closeSession(tmpChatSession)
        }
        this.chatSession = null
    }

    override fun onCleared() {
        super.onCleared()
        this.closeSession()
    }
}