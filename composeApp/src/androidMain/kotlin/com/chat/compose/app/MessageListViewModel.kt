package com.chat.compose.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.application.channel.database.android.AppMessageDatabase
import com.application.channel.im.*
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author liuzhongao
 * @since 2024/6/12 23:38
 */
class MessageListViewModel : ViewModel() {

    private val chatConnection: MsgConnectionService get() = MsgClient.getService(MsgConnectionService::class.java)
    private val chatService: MsgService get() = MsgClient.getService(MsgService::class.java)

    val flow = Pager(
        config = PagingConfig(pageSize = 20)
    ) {
        LimitTimestampPagingSource(
            chatterSessionId = "111111",
            sessionType = SessionType.P2P,
            timestamp = System.currentTimeMillis(),
            limit = 20,
            msgService = this.chatService
        )
    }.flow.cachedIn(viewModelScope)

    init {
        this.chatConnection.startService(
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

}

