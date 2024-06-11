package com.application.channel.im.session

import androidx.paging.PagingSource
import com.application.channel.im.MsgClient
import com.application.channel.im.MsgService
import com.application.channel.im.SessionExtensions
import com.application.channel.message.MessageReceiveListener
import com.application.channel.message.MessageReceiveListenerWrapper
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message

/**
 * @author liuzhongao
 * @since 2024/6/10 09:52
 */
internal abstract class AbstractChatSession(
    final override val sessionId: String,
    final override val sessionType: SessionType
) : ChatSession, MessageReceiveListener {

    private val receiveListenerWrapper = MessageReceiveListenerWrapper()

    override fun addListener(listener: MessageReceiveListener) {
        this.receiveListenerWrapper.addListener(listener)
    }

    override fun removeListener(listener: MessageReceiveListener) {
        this.receiveListenerWrapper.removeListener(listener)
    }

    override suspend fun fetchHistoryMessages(timestamp: Long, limit: Int): List<Message> {
        return MsgClient.getService(MsgService::class.java)
            .fetchMessages(this.sessionId, this.sessionType, timestamp, limit)
    }

    override suspend fun historyMessageSource(timestamp: Long, limit: Int): PagingSource<Long, Message> {
        return MsgClient.getService(MsgService::class.java)
            .fetchPagedMessages(this.sessionId, this.sessionType, timestamp, limit)
    }

    override suspend fun saveDraftContent(content: String) {
        MsgClient.getService(MsgService::class.java).updateSessionContactExtensions(this.sessionId, this.sessionType) {
            this[SessionExtensions.KEY_DRAFT_MESSAGE_STRING] = content
        }
    }

    final override fun onReceive(message: Message) {
        this.interceptMessageEvent(
            message = message,
            notify = this.receiveListenerWrapper::onReceive
        )
    }

    abstract fun interceptMessageEvent(message: Message, notify: (Message) -> Unit)

    fun performOnCreate() {
        this.onCreate()
    }

    fun performOnDestroy() {
        this.onDestroy()
    }

    protected abstract fun onCreate()

    protected abstract fun onDestroy()
}