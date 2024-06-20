package com.application.channel.message.database

import com.application.channel.database.AppMessageDatabase
import com.application.channel.database.meta.LocalSessionContact
import com.application.channel.message.SessionType
import com.application.channel.message.metadata.SessionContact
import com.application.channel.message.metadata.SessionContacts.toLocalSessionContact
import com.application.channel.message.metadata.SessionContacts.toSessionContact
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

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
}

internal fun SessionContactDatabaseApi(database: AppMessageDatabase?): SessionContactDatabaseApi {
    return SessionContactDatabaseImpl(database)
}

private class SessionContactDatabaseImpl(
    private val database: AppMessageDatabase?
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
        return sessionContact?.toSessionContact(database)
    }

    override suspend fun accessToSessionContact(sessionId: String, sessionType: SessionType) {
        val database = this.database ?: return
        val sessionContact = database.sessionContactDao.fetchSessionContact(sessionId, sessionType.value)
            ?.copy(lastModifyTimestamp = System.currentTimeMillis())
            ?: LocalSessionContact(sessionId, sessionType.value, System.currentTimeMillis())
        database.sessionContactDao.upsertSessionContact(sessionContact)
    }

    override suspend fun fetchRecentSessionContact(limit: Int): List<SessionContact> {
        val database = this.database ?: return emptyList()
        return coroutineScope {
            val sessionContactList = database.sessionContactDao.fetchRecentSessionContactList(limit)
            sessionContactList.map { sessionContact ->
                async { sessionContact.toSessionContact(database) }
            }.awaitAll()
        }
    }

    override fun fetchObservableSessionContact(limit: Int): Flow<List<SessionContact>> {
        val database = this.database ?: return flowOf(emptyList())
        return database.sessionContactDao.observableRecentSessionContacts(limit)
            .distinctUntilChanged()
            .map { localSessionContactList ->
                localSessionContactList.map { localSessionContact ->
                    localSessionContact.toSessionContact(this.database)
                }
            }
            .distinctUntilChanged()
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