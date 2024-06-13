package com.application.channel.message

import com.application.channel.database.OnTableChangedObserver
import com.application.channel.message.database.DBProvider
import com.application.channel.message.meta.Message
import com.application.channel.message.metadata.SessionContact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/6/10 13:58
 */
interface MessageRepository {

    suspend fun insertSessionContact(sessionId: String, sessionType: SessionType)
    suspend fun insertSessionContact(sessionContact: SessionContact)

    suspend fun updateSessionContact(sessionContact: SessionContact)
    suspend fun updateSessionContact(
        sessionId: String,
        sessionType: SessionType,
        function: SessionContact.() -> SessionContact
    )

    suspend fun deleteSessionContact(sessionId: String, sessionType: SessionType)
    suspend fun deleteSessionContact(sessionContact: SessionContact)

    suspend fun fetchSessionContact(sessionId: String, sessionType: SessionType): SessionContact?

    suspend fun fetchRecentSessionContactList(limit: Int): List<SessionContact>
    suspend fun fetchObservableSessionContactList(limit: Int): Flow<List<SessionContact>>

    suspend fun fetchMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long,
        limit: Int,
        further: Boolean
    ): List<Message>

    suspend fun insertMessages(messageList: List<Message>)

    suspend fun deleteMessage(uuid: String, sessionType: SessionType)

    fun addObserver(observer: OnTableChangedObserver)

}

internal class MessageRepositoryImpl @Inject constructor(
    private val databaseProvider: DBProvider
) : MessageRepository {

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
        this.databaseProvider.withTransaction {
            val sessionContact =
                this.databaseProvider.sessionContactDatabaseApi.findSessionContact(sessionId, sessionType)
            val updatedSessionContact = sessionContact?.run(function)
            if (updatedSessionContact != null) {
                this.databaseProvider.sessionContactDatabaseApi.updateSessionContact(updatedSessionContact)
            }
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

    override suspend fun fetchRecentSessionContactList(limit: Int): List<SessionContact> {
        return this.databaseProvider.sessionContactDatabaseApi.fetchRecentSessionContact(limit)
    }

    override suspend fun fetchObservableSessionContactList(limit: Int): Flow<List<SessionContact>> {
        return this.databaseProvider.sessionContactDatabaseApi.fetchObservableSessionContact(limit)
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

    override suspend fun insertMessages(messageList: List<Message>) {
        messageList.forEach { message ->
            this.databaseProvider.messageDatabaseApi.insertMessage(message)
        }
//        this.databaseProvider.withTransaction {
//        }
    }

    override suspend fun deleteMessage(uuid: String, sessionType: SessionType) {
        this.databaseProvider.messageDatabaseApi.deleteMessage(uuid, sessionType)
    }

    override fun addObserver(observer: OnTableChangedObserver) {
        this.databaseProvider.addTableChangedObserver(observer)
    }
}