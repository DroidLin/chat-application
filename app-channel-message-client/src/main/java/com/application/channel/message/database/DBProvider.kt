package com.application.channel.message.database

import com.application.channel.database.AppMessageDatabase
import com.application.channel.database.OnTableChangedObserver
import com.application.channel.message.meta.MessageParser
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/6/9 11:17
 */
interface DBProvider {

    val userSessionId: String

    val isDatabaseAvailable: Boolean

    val sessionContactDatabaseApi: SessionContactDatabaseApi

    val messageDatabaseApi: MessageDatabaseApi

    suspend fun withTransaction(function: suspend () -> Unit)

    fun addTableChangedObserver(observer: OnTableChangedObserver)
}

internal class DBProviderImpl @Inject constructor(
    private val database: AppMessageDatabase?,
    messageParser: MessageParser
) : DBProvider {

    override val userSessionId: String = database?.userSessionId ?: ""
    override val isDatabaseAvailable: Boolean = database != null

    override val sessionContactDatabaseApi: SessionContactDatabaseApi = SessionContactDatabaseApi(database)
    override val messageDatabaseApi: MessageDatabaseApi = MessageDatabaseApi(database, messageParser)

    override suspend fun withTransaction(function: suspend () -> Unit) {
        this.database?.withTransaction {
            function()
        }
    }

    override fun addTableChangedObserver(observer: OnTableChangedObserver) {
        this.database?.addTableObserver(observer)
    }

}