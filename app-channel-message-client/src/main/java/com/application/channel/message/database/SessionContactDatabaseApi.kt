package com.application.channel.message.database

import com.application.channel.database.AppMessageDatabase
import com.application.channel.database.LocalMessageTable
import com.application.channel.database.LocalSessionContactTable
import com.application.channel.database.OnTableChangedObserver
import com.application.channel.database.meta.LocalSessionContact
import com.application.channel.message.SessionType
import com.application.channel.message.meta.MessageParser
import com.application.channel.message.metadata.SessionContact
import com.application.channel.message.metadata.SessionContacts.toLocalSessionContact
import com.application.channel.message.metadata.SessionContacts.toSessionContact
import com.application.channel.message.util.LocalMessageConverter.toMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

/**
 * @author liuzhongao
 * @since 2024/6/9 11:17
 */
interface SessionContactDatabaseApi {

    suspend fun fetchRecentSessionContact(limit: Int): List<SessionContact>
    fun fetchObservableSessionContact(limit: Int): Flow<List<SessionContact>>

    suspend fun accessToSessionContact(sessionId: String, sessionType: SessionType)

    suspend fun deleteSessionContact(sessionId: String, sessionType: SessionType)
    suspend fun deleteSessionContact(sessionContact: SessionContact)

    suspend fun insertSessionContact(sessionContact: SessionContact)

    suspend fun updateSessionContact(sessionContact: SessionContact)

    suspend fun findSessionContact(sessionId: String, sessionType: SessionType): SessionContact?
    fun fetchObservableSessionContact(sessionId: String, sessionType: SessionType): Flow<SessionContact?>
}

internal fun SessionContactDatabaseApi(
    database: AppMessageDatabase?,
    messageParser: MessageParser
): SessionContactDatabaseApi {
    return SessionContactDatabaseImpl(database, messageParser)
}

private class SessionContactDatabaseImpl(
    private val database: AppMessageDatabase?,
    private val messageParser: MessageParser
) : SessionContactDatabaseApi {

    override suspend fun insertSessionContact(sessionContact: SessionContact) {
        val database = this.database ?: return
        val localSessionContact = sessionContact.toLocalSessionContact()
        database.sessionContactDao.insertSessionContact(localSessionContact)
    }

    override suspend fun updateSessionContact(sessionContact: SessionContact) {
        val database = this.database ?: return
        database.sessionContactDao.updateSessionContact(sessionContact.toLocalSessionContact())
    }

    override suspend fun findSessionContact(sessionId: String, sessionType: SessionType): SessionContact? {
        val database = this.database ?: return null
        val sessionContact = database.sessionContactDao.fetchSessionContact(sessionId, sessionType.value)
        return sessionContact?.toSessionContact(database, this.messageParser)
    }

    override fun fetchObservableSessionContact(sessionId: String, sessionType: SessionType): Flow<SessionContact?> {
        val database = this.database ?: return flowOf(null)
        val localSessionContactFlow =
            database.sessionContactDao.fetchObservableSessionContact(sessionId, sessionType.value)
        val recentMessageFlow = database.messageDao.observableRecentMessageWithSomeone(
            sessionId = database.userSessionId,
            chatterSessionId = sessionId,
            sessionTypeCode = sessionType.value
        )
        val unreadCountFlow = database.messageDao.observableCalculateUserSessionUnreadCount(
            sessionId = database.userSessionId,
            chatterSessionId = sessionId,
            sessionTypeCode = sessionType.value
        )
        return combine(
            localSessionContactFlow,
            recentMessageFlow,
            unreadCountFlow
        ) { localSessionContact, localMessage, unreadCount ->
            val message = localMessage?.toMessage()
            val parseMessage = if (message != null) {
                this.messageParser.parse(message)
            } else null
            localSessionContact?.toSessionContact(parseMessage, unreadCount)
        }.flowOn(Dispatchers.Default)
    }

    override suspend fun accessToSessionContact(sessionId: String, sessionType: SessionType) {
        val database = this.database ?: return
        val sessionContact = database.sessionContactDao.fetchSessionContact(sessionId, sessionType.value)
        if (sessionContact != null) return
        val newSessionContact = LocalSessionContact(sessionId, sessionType.value, System.currentTimeMillis())
        database.sessionContactDao.upsertSessionContact(newSessionContact)
    }

    override suspend fun fetchRecentSessionContact(limit: Int): List<SessionContact> {
        val database = this.database ?: return emptyList()
        return coroutineScope {
            val sessionContactList = database.sessionContactDao.fetchRecentSessionContactList(limit)
            sessionContactList.map { sessionContact ->
                async { sessionContact.toSessionContact(database, this@SessionContactDatabaseImpl.messageParser) }
            }.awaitAll()
        }
    }

    override fun fetchObservableSessionContact(limit: Int): Flow<List<SessionContact>> {
        val database = this.database ?: return flowOf(emptyList())

        val channelEvent = Channel<Unit>(Channel.CONFLATED)
        val observer = object : OnTableChangedObserver {
            override val tables: List<String> = listOf(LocalSessionContactTable.TABLE_NAME, LocalMessageTable.TABLE_NAME)
            override fun onTableChanged(tableNames: Set<String>) {
                channelEvent.trySend(Unit)
            }
        }

        channelEvent.trySend(Unit)
        return flow {
            database.addTableObserver(observer)
            for (event in channelEvent) {
                val localSessionContactList = database.sessionContactDao.fetchRecentSessionContactList(limit)
                val sessionContactList = localSessionContactList.map { localSessionContact ->
                    localSessionContact.toSessionContact(database, this@SessionContactDatabaseImpl.messageParser)
                }
                emit(sessionContactList)
            }
            database.removeTableObserver(observer)
        }.distinctUntilChanged()
    }

    override suspend fun deleteSessionContact(sessionId: String, sessionType: SessionType) {
        val database = this.database ?: return
        val localSessionContact = database.sessionContactDao.fetchSessionContact(sessionId, sessionType.value)
        if (localSessionContact != null) {
            database.sessionContactDao.deleteSessionContact(localSessionContact)
        }
    }

    override suspend fun deleteSessionContact(sessionContact: SessionContact) {
        val database = this.database ?: return
        database.sessionContactDao.deleteSessionContact(sessionContact.toLocalSessionContact())
    }
}