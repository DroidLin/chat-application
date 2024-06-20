package com.chat.compose.app.screen.message.vm

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.application.channel.im.MsgConnectionService
import com.application.channel.im.session.ChatSession
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import com.chat.compose.app.metadata.UiMessage
import com.chat.compose.app.metadata.UiSessionContact
import com.chat.compose.app.metadata.toUiMessage
import com.chat.compose.app.usecase.CloseSessionUseCase
import com.chat.compose.app.usecase.OpenChatSessionUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/6/17 23:55
 */
class SessionDetailViewModel constructor(
    private val openChatSessionUseCase: OpenChatSessionUseCase,
    private val closeSessionUseCase: CloseSessionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SessionDetailState())
    val state: StateFlow<SessionDetailState> = this._state.asStateFlow()

    private var chatSession: ChatSession? = null

    fun onTextChanged(text: String) {
        this._state.update { it.copy(inputText = text) }
    }

    fun openSession(sessionId: String, sessionType: SessionType): StateFlow<PagingData<UiMessage>> {
        this.closeSession()
        val chatSession = this.openChatSessionUseCase.openChatSession(sessionId, sessionType)
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
            )
                .flow
                .map { pagingData -> pagingData.map(Message::toUiMessage) }
                .distinctUntilChanged()
                .stateIn(this.viewModelScope, SharingStarted.Lazily, PagingData.empty())
        } finally {
            this.chatSession = chatSession
        }
    }

    fun saveDraft() {
        val inputText = this._state.value.inputText
        val chatSession = this.chatSession ?: return
        this.viewModelScope.launch {
            chatSession.saveDraftContent(inputText)
        }
    }

    fun closeSession() {
        val tmpChatSession = this.chatSession
        if (tmpChatSession != null) {
            this.closeSessionUseCase.closeSession(tmpChatSession)
        }
        this.chatSession = null
    }

    override fun onCleared() {
        super.onCleared()
        this.closeSession()
    }
}

@Immutable
data class SessionDetailState(
    val isLoading: Boolean = false,
    val sessionContact: UiSessionContact? = null,
    val inputText: String = "",
)