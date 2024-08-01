package com.chat.compose.app.screen.message.vm

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import com.application.channel.im.draftMessage
import com.application.channel.im.session.ChatSession
import com.application.channel.message.SessionType
import com.chat.compose.app.metadata.UiMessageItem
import com.chat.compose.app.platform.viewmodel.AbstractStatefulViewModel
import com.chat.compose.app.platform.viewmodel.Event
import com.chat.compose.app.platform.viewmodel.State
import com.chat.compose.app.usecase.CloseSessionUseCase
import com.chat.compose.app.usecase.FetchChatDetailListUseCase
import com.chat.compose.app.usecase.FetchRecentContactUseCase
import com.chat.compose.app.usecase.OpenChatSessionUseCase
import com.chat.compose.app.util.mainCoroutineScope
import com.chat.compose.app.util.profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

/**
 * @author liuzhongao
 * @since 2024/6/17 23:55
 */
class SessionDetailViewModel constructor(
    private val openChatSessionUseCase: OpenChatSessionUseCase,
    private val closeSessionUseCase: CloseSessionUseCase,
    private val fetchRecentContactUseCase: FetchRecentContactUseCase,
    private val fetchChatDetailListUseCase: FetchChatDetailListUseCase,
) : AbstractStatefulViewModel<ChatDetailUiState, ChatDetailEvent>() {

    private var chatSession: ChatSession? = null

    override val initialState: ChatDetailUiState get() = ChatDetailUiState()

    var inputText by mutableStateOf("")

    suspend fun openSession(sessionId: String, sessionType: SessionType) {
        this.closeSession()
        val recentContact = this.fetchRecentContactUseCase.fetchRecentContactOrCreate(sessionId, sessionType)
        val chatSession = this.openChatSessionUseCase.openChatSession(sessionId, sessionType)
        val pagerListFlow = this.fetchChatDetailListUseCase.openPagerListFlow(chatSession)

        this.inputText = recentContact?.draftMessage ?: ""
        updateState {
            ChatDetailUiState(
                showingTitle = recentContact?.profile?.userInfo?.userName ?: "",
                flow = pagerListFlow
            )
        }
    }

    fun saveDraft() {
        val inputText = this.inputText
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

    fun updateInputText(text: String) {
        this.inputText = text
    }

    fun onSendTextMessage() {
        val chatSession = this.chatSession ?: return
        val inputText = this.inputText
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
interface ChatDetailEvent : Event

@Immutable
data class ChatDetailUiState(
    val showingTitle: String = "",
    val flow: Flow<PagingData<UiMessageItem>> = flowOf(PagingData.empty())
) : State.Success