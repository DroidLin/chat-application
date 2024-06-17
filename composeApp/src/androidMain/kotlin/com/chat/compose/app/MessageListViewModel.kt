package com.chat.compose.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.application.channel.database.android.AppMessageDatabase
import com.application.channel.im.*
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * @author liuzhongao
 * @since 2024/6/12 23:38
 */
class MessageListViewModel : ViewModel() {

    private val chatConnection: MsgConnectionService get() = MsgClient.getService(MsgConnectionService::class.java)
    private val chatService: MsgService get() = MsgClient.getService(MsgService::class.java)

    val flow = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 5,
            enablePlaceholders = false,
            initialLoadSize = 10,
        ),
    ) {
        this.chatService.fetchPagedMessages(
            chatterSessionId = "111111",
            sessionType = SessionType.P2P,
            anchor = null,
        )
    }
        .flow.map { pagingData ->
            pagingData.map { message ->
                convertMessageToUiMessage(message)
            }
        }
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

    init {
        this.chatConnection.initService(
            initConfig = IMInitConfig(
                remoteAddress = "http://127.0.0.1:8080",
                token = Token("222222", "222222"),
                factory = { sessionId ->
                    AppMessageDatabase(
                        context = App.applicationContext,
                        sessionId = sessionId
                    )
                }
            )
        )
    }

    fun deleteMessage(uuid: String, sessionType: SessionType) {
        this.viewModelScope.launch {
            this@MessageListViewModel.chatService.deleteMessage(uuid, sessionType)
        }
    }

    fun insertMessage(messageList: List<Message>) {
        this.viewModelScope.launch(Dispatchers.IO) {
            this@MessageListViewModel.chatService.insertMessages(messageList)
        }
    }

    fun insertMessage(message: Message) {
        this.viewModelScope.launch(Dispatchers.IO) {
            this@MessageListViewModel.chatService.insertMessage(message)
        }
    }

    override fun onCleared() {
        super.onCleared()
        this.chatConnection.stopService()
        this.chatService
    }

}

