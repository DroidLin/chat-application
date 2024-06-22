package com.application.channel.im.session

import androidx.paging.PagingSource
import com.application.channel.im.MsgClient
import com.application.channel.im.MsgConnectionService
import com.application.channel.im.MsgService
import com.application.channel.im.SessionExtensions
import com.application.channel.message.Callback
import com.application.channel.message.MessageReceiveListener
import com.application.channel.message.MessageReceiveListenerWrapper
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import com.application.channel.message.meta.Messages

/**
 * @author liuzhongao
 * @since 2024/6/10 09:52
 */
internal abstract class AbstractChatSession(
    override val context: ChatSessionContext
) : ChatSession, MessageReceiveListener {

    private val receiveListenerWrapper = MessageReceiveListenerWrapper()

    private val sessionId: String get() = this.context.chatterSessionId
    private val sessionType: SessionType get() = this.context.chatterSessionType

    override fun addListener(listener: MessageReceiveListener) {
        this.receiveListenerWrapper.addListener(listener)
    }

    override fun removeListener(listener: MessageReceiveListener) {
        this.receiveListenerWrapper.removeListener(listener)
    }

    override fun sendTextMessage(text: String) {
        val textMessage = Messages.buildTextMessage(
            content = text,
            selfSessionId = this.context.selfUserSessionId,
            sessionId = this.context.chatterSessionId,
            sessionType = this.context.chatterSessionType
        )
        MsgClient.getService(MsgConnectionService::class.java).writeMessage(textMessage, Callback)
    }

    override suspend fun fetchHistoryMessages(timestamp: Long, limit: Int): List<Message> {
        return MsgClient.getService(MsgService::class.java)
            .fetchMessages(this.sessionId, this.sessionType, timestamp, limit)
    }

    override fun historyMessageSource(timestamp: Long, limit: Int): PagingSource<Long, Message> {
        return MsgClient.getService(MsgService::class.java)
            .fetchPagedMessages(this.sessionId, this.sessionType, timestamp, limit)
    }

    override fun historyMessageSource(anchorMessage: Message?, limit: Int): PagingSource<Message, Message> {
        return MsgClient.getService(MsgService::class.java)
            .fetchPagedMessages(this.sessionId, this.sessionType, anchorMessage)
    }

    override suspend fun saveDraftContent(content: String) {
        val msgService = MsgClient.getService(MsgService::class.java)
        msgService.updateSessionContactExtensions(this.sessionId, this.sessionType) {
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

    internal fun performOnCreate() {
        this.onCreate()
    }

    internal fun performOnDestroy() {
        this.onDestroy()
    }

    protected abstract fun onCreate()

    protected abstract fun onDestroy()
}