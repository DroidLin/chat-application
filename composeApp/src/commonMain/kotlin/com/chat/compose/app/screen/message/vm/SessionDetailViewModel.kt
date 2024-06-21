package com.chat.compose.app.screen.message.vm

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.application.channel.im.draftMessage
import com.application.channel.im.session.ChatSession
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import com.application.channel.message.metadata.SessionContact
import com.chat.compose.app.metadata.UiMessage
import com.chat.compose.app.metadata.toUiMessage
import com.chat.compose.app.metadata.toUiSessionContact
import com.chat.compose.app.usecase.CloseSessionUseCase
import com.chat.compose.app.usecase.FetchSessionContactUseCase
import com.chat.compose.app.usecase.OpenChatSessionUseCase
import com.chat.compose.app.util.accessFirst
import com.chat.compose.app.util.collect
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author liuzhongao
 * @since 2024/6/17 23:55
 */
class SessionDetailViewModel constructor(
    private val openChatSessionUseCase: OpenChatSessionUseCase,
    private val closeSessionUseCase: CloseSessionUseCase,
    private val fetchSessionContactUseCase: FetchSessionContactUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SessionDetailState())
    val state: StateFlow<SessionDetailState> = this._state.asStateFlow()

    private var chatSession: ChatSession? = null
    private var observerJob: Job? = null

    fun openSession(sessionId: String, sessionType: SessionType): StateFlow<PagingData<UiMessage>> {
        this.observerJob?.cancel()
        this.observerJob = this.fetchSessionContactUseCase
            .fetchObservableSessionContact(sessionId, sessionType)
            .accessFirst { sessionContact ->
                val draftMessage= sessionContact?.draftMessage
                if (!draftMessage.isNullOrBlank()) {
                    this._state.update { it.copy(inputText = draftMessage) }
                }
            }
            .map { sessionContact -> sessionContact?.toUiSessionContact() }
            .filterNotNull()
            .distinctUntilChanged()
            .onEach { uiSessionContact ->
                this._state.update { state -> state.copy(title = uiSessionContact.sessionContactName) }
            }
            .collect(this.viewModelScope)

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

    fun release() {
        this.closeSession()
        this.observerJob?.cancel()
        this.observerJob = null
    }

    fun updateInputText(text: String) {
        this._state.update { it.copy(inputText = text) }
    }

    override fun onCleared() {
        super.onCleared()
        this.closeSession()
    }
}

@Immutable
data class SessionDetailState(
    val isLoading: Boolean = false,
    val sessionContact: SessionContact? = null,
    val title: String = "",
    val inputText: String = "",
)