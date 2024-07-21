package com.application.channel.im

import androidx.paging.PagingSource
import com.application.channel.database.AppMessageDatabase
import com.application.channel.database.OnTableChangedObserver
import com.application.channel.message.MessageRepository
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import com.application.channel.message.metadata.MutableSessionContact
import com.application.channel.message.metadata.SessionContact
import com.application.channel.message.metadata.immutableSessionContact
import com.application.channel.message.metadata.mutableSessionContact
import kotlinx.coroutines.flow.Flow

/**
 * @author liuzhongao
 * @since 2024/6/10 10:55
 */
fun MsgService(): MsgService {
    return MsgServiceImpl()
}

private class MsgServiceImpl : MsgService {

    private val messageRepository: MessageRepository
        get() = MsgClient.getService(MsgConnectionService::class.java).messageRepository

    override suspend fun <T> withTransaction(readOnly: Boolean, block: suspend AppMessageDatabase.Transaction<T>.(MsgService) -> T): T {
        return this.messageRepository.withTransaction(readOnly) {
            block(this@MsgServiceImpl)
        }
    }

    override suspend fun insertSessionContact(sessionId: String, sessionType: SessionType) {
        this.messageRepository.insertSessionContact(sessionId, sessionType)
    }

    override suspend fun insertSessionContact(
        sessionId: String,
        sessionType: SessionType,
        function: MutableMap<String, Any?>.() -> Unit
    ) {
        this.messageRepository.updateSessionContact(sessionId, sessionType) {
            mutableSessionContact()
                .also { it.extensions.apply(function) }
                .immutableSessionContact()
        }
    }

    override suspend fun updateSessionContact(
        sessionId: String,
        sessionType: SessionType,
        function: MutableSessionContact.() -> Unit
    ) {
        this.messageRepository.updateSessionContact(sessionId, sessionType) {
            mutableSessionContact()
                .apply(function)
                .immutableSessionContact()
        }
    }

    override suspend fun insertSessionContact(sessionContact: SessionContact) {
        this.messageRepository.insertSessionContact(sessionContact)
    }

    override suspend fun updateSessionContact(sessionContact: SessionContact) {
        this.messageRepository.updateSessionContact(
            sessionContact.sessionId,
            sessionContact.sessionType
        ) { sessionContact }
    }

    override suspend fun updateSessionContactExtensions(
        sessionId: String,
        sessionType: SessionType,
        function: MutableMap<String, Any?>.() -> Unit
    ) {
        this.updateSessionContactExtensions(sessionId, sessionType, true, function)
    }

    override suspend fun updateSessionContactExtensions(
        sessionId: String,
        sessionType: SessionType,
        updateAccess: Boolean,
        function: MutableMap<String, Any?>.() -> Unit
    ) {
        this.messageRepository.updateSessionContact(sessionId, sessionType, updateAccess) {
            mutableSessionContact()
                .also { it.extensions.apply(function) }
                .immutableSessionContact()
        }
    }

    override suspend fun fetchSessionContact(sessionId: String, sessionType: SessionType): SessionContact? {
        return this.messageRepository.fetchSessionContact(sessionId, sessionType)
    }

    override fun fetchObservableSessionContact(sessionId: String, sessionType: SessionType): Flow<SessionContact?> {
        return this.messageRepository.fetchObservableSessionContact(sessionId, sessionType)
    }

    override suspend fun deleteSessionContact(sessionId: String, sessionType: SessionType) {
        this.messageRepository.deleteSessionContact(sessionId, sessionType)
    }

    override suspend fun deleteSessionContact(sessionContact: SessionContact) {
        this.messageRepository.deleteSessionContact(sessionContact)
    }

    override suspend fun fetchRecentSessionContactList(limit: Int): List<SessionContact> {
        return this.messageRepository.fetchRecentSessionContactList(limit)
    }

    override fun fetchObservableRecentSessionContactList(limit: Int): Flow<List<SessionContact>> {
        return this.messageRepository.fetchObservableSessionContactList(limit)
    }

    override suspend fun insertMessage(message: Message) {
        this.messageRepository.insertMessage(message)
    }

    override suspend fun insertMessages(messageList: List<Message>) {
        this.messageRepository.insertMessages(messageList)
    }

    override suspend fun updateMessage(message: Message) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMessage(message: Message) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMessage(uuid: String, sessionType: SessionType) {
        this.messageRepository.deleteMessage(uuid, sessionType)
    }

    override suspend fun fetchMessagesAtTime(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long
    ): List<Message> {
        return this.messageRepository.fetchMessagesAtTime(
            chatterSessionId = chatterSessionId,
            sessionType = sessionType,
            timestamp = timestamp,
        )
    }

    override suspend fun fetchMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long,
        limit: Int,
        further: Boolean
    ): List<Message> {
        return this.messageRepository.fetchMessages(
            chatterSessionId = chatterSessionId,
            sessionType = sessionType,
            timestamp = timestamp,
            limit = limit,
            further = further,
        )
    }

    override suspend fun fetchObservableMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long,
        limit: Int
    ): Flow<List<Message>> {
        TODO("Not yet implemented")
    }

    override fun fetchPagedMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long,
        limit: Int
    ): PagingSource<Long, Message> {
        return MessageLimitTimestampPagingSource(
            chatterSessionId = chatterSessionId,
            sessionType = sessionType,
            timestamp = timestamp,
            limit = limit,
            messageRepository = this.messageRepository
        )
    }

    override fun fetchPagedMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        anchor: Message?,
    ): PagingSource<Message, Message> {
        return MessagePagingSource(
            chatterSessionId = chatterSessionId,
            sessionType = sessionType,
            messageRepository = this.messageRepository,
            anchor = anchor,
        )
    }

    override suspend fun markMessageAsRead(message: Message) {
        TODO("Not yet implemented")
    }

    override suspend fun markMessageAsRead(uuid: String, sessionType: SessionType) {
        TODO("Not yet implemented")
    }

    override suspend fun clearUnreadCount(sessionId: String, sessionType: SessionType) {
        TODO("Not yet implemented")
    }

    override fun addObserver(observer: OnTableChangedObserver) {
        this.messageRepository.addObserver(observer)
    }

    override fun removeObserver(observer: OnTableChangedObserver) {
        this.messageRepository.removeObserver(observer)
    }
}