package com.application.channel.message

import com.application.channel.database.AppMessageDatabase.Transaction
import com.application.channel.database.OnTableChangedObserver
import com.application.channel.message.database.DBProvider
import com.application.channel.message.meta.Message
import com.application.channel.message.metadata.SessionContact
import com.application.channel.message.metadata.immutableSessionContact
import com.application.channel.message.metadata.mutableSessionContact
import kotlinx.coroutines.flow.Flow

/**
 * @author liuzhongao
 * @since 2024/6/10 13:58
 */
interface MessageRepository {

    val useSessionId: String

    suspend fun <T> withTransaction(readOnly: Boolean, block: suspend Transaction<T>.() -> T): T

    suspend fun insertSessionContact(sessionId: String, sessionType: SessionType)
    suspend fun insertSessionContact(sessionContact: SessionContact)

    suspend fun updateSessionContact(sessionContact: SessionContact)
    suspend fun updateSessionContact(
        sessionId: String,
        sessionType: SessionType,
        function: SessionContact.() -> SessionContact
    )
    suspend fun updateSessionContact(
        sessionId: String,
        sessionType: SessionType,
        updateAccess: Boolean,
        function: SessionContact.() -> SessionContact
    )

    suspend fun deleteSessionContact(sessionId: String, sessionType: SessionType)
    suspend fun deleteSessionContact(sessionContact: SessionContact)

    suspend fun fetchSessionContact(sessionId: String, sessionType: SessionType): SessionContact?
    fun fetchObservableSessionContact(sessionId: String, sessionType: SessionType): Flow<SessionContact?>

    suspend fun fetchRecentSessionContactList(limit: Int): List<SessionContact>
    fun fetchObservableSessionContactList(limit: Int): Flow<List<SessionContact>>

    suspend fun fetchMessagesAtTime(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long
    ): List<Message>

    suspend fun fetchMessagesAtTime(
        chatterSessionId: String,
        sessionType: SessionType,
        anchor: Message?
    ): List<Message>

    suspend fun fetchMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long,
        limit: Int,
        further: Boolean
    ): List<Message>

    suspend fun fetchMessagesWithId(
        chatterSessionId: String,
        sessionType: SessionType,
        limit: Int,
        further: Boolean,
        anchor: Message?
    ): List<Message>

    suspend fun insertMessage(message: Message)
    suspend fun insertMessages(messageList: List<Message>)

    suspend fun deleteMessage(uuid: String, sessionType: SessionType)

    fun addObserver(observer: OnTableChangedObserver)
    fun removeObserver(observer: OnTableChangedObserver)

    fun release()
}

fun MessageRepository(dbProvider: DBProvider): MessageRepository {
    return MessageRepositoryImpl(dbProvider)
}

private class MessageRepositoryImpl(
    private val databaseProvider: DBProvider
) : MessageRepository {

    override val useSessionId: String get() = this.databaseProvider.userSessionId

    override suspend fun <T> withTransaction(readOnly: Boolean, block: suspend Transaction<T>.() -> T): T {
        return this.databaseProvider.withTransaction(readOnly, block)
    }

    override suspend fun insertSessionContact(sessionId: String, sessionType: SessionType) {
        val sessionContact = SessionContact(sessionId = sessionId, sessionType = sessionType)
        this.insertSessionContact(sessionContact)
    }

    override suspend fun insertSessionContact(sessionContact: SessionContact) {
        this.databaseProvider.sessionContactDatabaseApi.insertSessionContact(sessionContact)
    }

    override suspend fun updateSessionContact(sessionContact: SessionContact) {
        this.databaseProvider.sessionContactDatabaseApi.updateSessionContact(sessionContact)
    }

    override suspend fun updateSessionContact(
        sessionId: String,
        sessionType: SessionType,
        function: SessionContact.() -> SessionContact
    ) {
        this.updateSessionContact(sessionId, sessionType, true, function)
    }

    override suspend fun updateSessionContact(
        sessionId: String,
        sessionType: SessionType,
        updateAccess: Boolean,
        function: SessionContact.() -> SessionContact
    ) {
        val sessionContact = this.databaseProvider.sessionContactDatabaseApi.findSessionContact(sessionId, sessionType)
        val updatedSessionContact = sessionContact?.run(function)
        if (sessionContact != null && updatedSessionContact != null && sessionContact != updatedSessionContact) {
            val immutableSessionContact = (if (updateAccess) {
                updatedSessionContact.mutableSessionContact().apply {
                    this.lastUpdateTimestamp = System.currentTimeMillis()
                }
            } else updatedSessionContact).immutableSessionContact()
            this.databaseProvider.sessionContactDatabaseApi.updateSessionContact(immutableSessionContact)
        }
    }

    override suspend fun deleteSessionContact(sessionId: String, sessionType: SessionType) {
        this.databaseProvider.sessionContactDatabaseApi.deleteSessionContact(sessionId, sessionType)
    }

    override suspend fun deleteSessionContact(sessionContact: SessionContact) {
        this.databaseProvider.sessionContactDatabaseApi.deleteSessionContact(sessionContact)
    }

    override suspend fun fetchSessionContact(sessionId: String, sessionType: SessionType): SessionContact? {
        return this.databaseProvider.sessionContactDatabaseApi.findSessionContact(sessionId, sessionType)
    }

    override fun fetchObservableSessionContact(
        sessionId: String,
        sessionType: SessionType
    ): Flow<SessionContact?> {
        return this.databaseProvider.sessionContactDatabaseApi.fetchObservableSessionContact(sessionId, sessionType)
    }

    override suspend fun fetchRecentSessionContactList(limit: Int): List<SessionContact> {
        return this.databaseProvider.sessionContactDatabaseApi.fetchRecentSessionContact(limit)
    }

    override fun fetchObservableSessionContactList(limit: Int): Flow<List<SessionContact>> {
        return this.databaseProvider.sessionContactDatabaseApi.fetchObservableSessionContact(limit)
    }

    override suspend fun fetchMessagesAtTime(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long
    ): List<Message> {
        return this.databaseProvider.messageDatabaseApi.queryMessagesAtTime(
            chatterSessionId = chatterSessionId,
            sessionType = sessionType,
            timestamp = timestamp,
        )
    }

    override suspend fun fetchMessagesAtTime(
        chatterSessionId: String,
        sessionType: SessionType,
        anchor: Message?
    ): List<Message> {
        return this.databaseProvider.messageDatabaseApi.queryMessagesAtTime(
            chatterSessionId = chatterSessionId,
            sessionType = sessionType,
            anchor = anchor,
        )
    }

    override suspend fun fetchMessagesWithId(
        chatterSessionId: String,
        sessionType: SessionType,
        limit: Int,
        further: Boolean,
        anchor: Message?
    ): List<Message> {
        return this.databaseProvider.messageDatabaseApi.queryMessages(
            chatterSessionId = chatterSessionId,
            sessionType = sessionType,
            anchor = anchor,
            limit = limit,
            further = further,
        )
    }

    override suspend fun fetchMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long,
        limit: Int,
        further: Boolean
    ): List<Message> {
        return this.databaseProvider.messageDatabaseApi.queryMessages(
            chatterSessionId = chatterSessionId,
            sessionType = sessionType,
            timestamp = timestamp,
            limit = limit,
            further = further,
        )
    }

    override suspend fun insertMessage(message: Message) {
        this.databaseProvider.messageDatabaseApi.insertMessage(message)
    }

    override suspend fun insertMessages(messageList: List<Message>) {
        this.databaseProvider.messageDatabaseApi.insertMessage(messageList)
    }

    override suspend fun deleteMessage(uuid: String, sessionType: SessionType) {
        this.databaseProvider.messageDatabaseApi.deleteMessage(uuid, sessionType)
    }

    override fun addObserver(observer: OnTableChangedObserver) {
        this.databaseProvider.addTableChangedObserver(observer)
    }

    override fun removeObserver(observer: OnTableChangedObserver) {
        this.databaseProvider.removeTableChangedObserver(observer)
    }

    override fun release() {
        this.databaseProvider.release()
    }
}