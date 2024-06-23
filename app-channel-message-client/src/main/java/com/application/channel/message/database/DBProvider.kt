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

    fun addTableChangedObserver(observer: OnTableChangedObserver)
    fun removeTableChangedObserver(observer: OnTableChangedObserver)

    fun release()
}

internal fun DBProvider(
    database: AppMessageDatabase?,
    messageParser: MessageParser
): DBProvider {
    return DBProviderImpl(database, messageParser)
}

private class DBProviderImpl(
    private val database: AppMessageDatabase?,
    messageParser: MessageParser
) : DBProvider {

    override val userSessionId: String get() = this.database?.userSessionId ?: ""
    override val isDatabaseAvailable: Boolean get() = this.database != null

    override val sessionContactDatabaseApi: SessionContactDatabaseApi =
        SessionContactDatabaseApi(this.database, messageParser)
    override val messageDatabaseApi: MessageDatabaseApi = MessageDatabaseApi(this.database, messageParser)

    override fun addTableChangedObserver(observer: OnTableChangedObserver) {
        this.database?.addTableObserver(observer)
    }

    override fun removeTableChangedObserver(observer: OnTableChangedObserver) {
        this.database?.removeTableObserver(observer)
    }

    override fun release() {
        this.database?.release()
    }
}