package com.chat.compose.app.screen.message.vm

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.application.channel.im.draftMessage
import com.application.channel.im.session.ChatSession
import com.application.channel.message.SessionType
import com.application.channel.message.metadata.RecentContact
import com.chat.compose.app.metadata.UiMessageItem
import com.chat.compose.app.metadata.toUiSessionContact
import com.chat.compose.app.usecase.CloseSessionUseCase
import com.chat.compose.app.usecase.FetchChatDetailListUseCase
import com.chat.compose.app.usecase.FetchSessionContactUseCase
import com.chat.compose.app.usecase.OpenChatSessionUseCase
import com.chat.compose.app.util.collect
import com.chat.compose.app.util.mainCoroutineScope
import com.chat.compose.app.util.onFirst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    private val fetchSessionContactUseCase: FetchSessionContactUseCase,
    private val fetchChatDetailListUseCase: FetchChatDetailListUseCase,
) : ViewModel() {

    private val coroutineScope = CoroutineScope(this.viewModelScope.coroutineContext + Dispatchers.Default)

    private val _state = MutableStateFlow(RecentDetailState())
    val state: StateFlow<RecentDetailState> = this._state.asStateFlow()

    private var chatSession: ChatSession? = null
    private var observerJob: Job? = null

    fun openSession(sessionId: String, sessionType: SessionType): StateFlow<PagingData<UiMessageItem>> {
        this.observerJob?.cancel()
        this.observerJob = this.fetchSessionContactUseCase
            .fetchObservableRecentContactOrCreate(sessionId, sessionType)
            .onFirst { recentContact ->
                val draftMessage = recentContact.draftMessage
                if (!draftMessage.isNullOrBlank()) {
                    this._state.update { it.copy(inputText = draftMessage) }
                }
            }
            .map { sessionContact -> sessionContact.toUiSessionContact() }
            .filterNotNull()
            .distinctUntilChanged()
            .onEach { uiSessionContact ->
                this._state.update { state -> state.copy(title = uiSessionContact.sessionContactName) }
            }
            .collect(this.coroutineScope)

        this.closeSession()
        val chatSession = this.openChatSessionUseCase.openChatSession(sessionId, sessionType)
        try {
            return this.fetchChatDetailListUseCase.openPagerListFlow(chatSession)
                .stateIn(this.coroutineScope, SharingStarted.Lazily, PagingData.empty())
        } finally {
            this.chatSession = chatSession
        }
    }

    fun saveDraft() {
        val inputText = this._state.value.inputText
        val chatSession = this.chatSession ?: return
        mainCoroutineScope.launch {
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

    fun onSendTextMessage() {
        val chatSession = this.chatSession ?: return
        val inputText = this._state.getAndUpdate { it.copy(inputText = "") }.inputText
        if (inputText.isBlank()) {
            return
        }
        chatSession.sendTextMessage(inputText)
    }

    override fun onCleared() {
        super.onCleared()
        this.closeSession()
    }
}

@Immutable
data class RecentDetailState(
    val isLoading: Boolean = false,
    val recentContact: RecentContact? = null,
    val title: String = "",
    val inputText: String = "",
)