package com.application.channel.message.database

import com.application.channel.database.AppMessageDatabase
import com.application.channel.database.LocalMessageTable
import com.application.channel.database.LocalRecentContactTable
import com.application.channel.database.OnTableChangedObserver
import com.application.channel.database.meta.LocalRecentContact
import com.application.channel.database.meta.LocalSessionContact
import com.application.channel.message.SessionType
import com.application.channel.message.meta.MessageParser
import com.application.channel.message.metadata.RecentContact
import com.application.channel.message.metadata.RecentContacts.toLocalRecentContact
import com.application.channel.message.metadata.RecentContacts.toRecentContact
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * @author liuzhongao
 * @since 2024/7/24 23:20
 */
interface RecentContactDatabaseApi {

    suspend fun accessToRecentContact(sessionId: String, sessionType: SessionType)

    fun fetchRecentContactFlow(limit: Int): Flow<List<RecentContact>>
    suspend fun fetchRecentContacts(limit: Int): List<RecentContact>

    suspend fun insertRecentContact(recentContact: RecentContact)

    suspend fun fetchRecentContact(sessionId: String, sessionType: SessionType): RecentContact?
    fun fetchRecentContactFlow(sessionId: String, sessionType: SessionType): Flow<RecentContact?>

    suspend fun updateRecentContact(recentContact: RecentContact)

    suspend fun deleteRecentContact(recentContact: RecentContact)
}

internal fun RecentContactDatabaseApi(
    database: AppMessageDatabase?,
    messageParser: MessageParser
): RecentContactDatabaseApi = RecentContactDatabaseImpl(database, messageParser)

private class RecentContactDatabaseImpl(
    private val database: AppMessageDatabase?,
    private val messageParser: MessageParser,
) : RecentContactDatabaseApi {

    override suspend fun accessToRecentContact(sessionId: String, sessionType: SessionType) {
        val database = this.database ?: return
        val recentContact = database.recentContactDao.fetchRecentContact(sessionId, sessionType.value)
        if (recentContact != null) return
        val newRecentContact = LocalRecentContact(sessionId, sessionType.value, System.currentTimeMillis(), null)
        database.recentContactDao.upsert(newRecentContact)
    }

    override fun fetchRecentContactFlow(limit: Int): Flow<List<RecentContact>> {
        val database = this.database ?: return flowOf(emptyList())
        val channelEvent = Channel<Unit>(Channel.CONFLATED)
        val observer = object : OnTableChangedObserver {
            override val tables: List<String> = listOf(LocalRecentContactTable.TABLE_NAME, LocalMessageTable.TABLE_NAME)
            override fun onTableChanged(tableNames: Set<String>) {
                channelEvent.trySend(Unit)
            }
        }

        channelEvent.trySend(Unit)
        return flow {
            database.addTableObserver(observer)
            for (event in channelEvent) {
                val localRecentContactList = database.recentContactDao.fetchRecentContactList(limit)
                val recentContactList = localRecentContactList.map { localRecentContact ->
                    localRecentContact.toRecentContact(database, this@RecentContactDatabaseImpl.messageParser)
                }
                emit(recentContactList)
            }
            database.removeTableObserver(observer)
        }.distinctUntilChanged()
    }

    override suspend fun fetchRecentContacts(limit: Int): List<RecentContact> {
        val database = this.database ?: return emptyList()
        val localRecentContactList = database.recentContactDao.fetchRecentContactList(limit)
        val recentContactList = localRecentContactList.map { localRecentContact ->
            localRecentContact.toRecentContact(database, this@RecentContactDatabaseImpl.messageParser)
        }
        return recentContactList
    }

    override suspend fun insertRecentContact(recentContact: RecentContact) {
        val database = this.database ?: return
        database.recentContactDao.insert(recentContact.toLocalRecentContact())
    }

    override fun fetchRecentContactFlow(sessionId: String, sessionType: SessionType): Flow<RecentContact?> {
        val database = this.database ?: return flowOf(null)
        return database.recentContactDao.fetchObservableRecentContact(sessionId, sessionType.value)
            .map { it?.toRecentContact(database, this.messageParser) }
            .distinctUntilChanged()
    }

    override suspend fun fetchRecentContact(sessionId: String, sessionType: SessionType): RecentContact? {
        val database = this.database ?: return null
        val localRecentContact = database.recentContactDao.fetchRecentContact(sessionId, sessionType.value)
        return localRecentContact?.toRecentContact(database, this.messageParser)
    }

    override suspend fun updateRecentContact(recentContact: RecentContact) {
        val database = this.database ?: return
        database.recentContactDao.update(recentContact.toLocalRecentContact())
    }

    override suspend fun deleteRecentContact(recentContact: RecentContact) {
        val database = this.database ?: return
        database.recentContactDao.delete(recentContact.toLocalRecentContact())
    }
}