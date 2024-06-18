package com.application.channel.database

import androidx.room.*
import com.application.channel.database.dao.LocalMessageDao
import com.application.channel.database.dao.LocalSessionContactDao
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentHashMap

/**
 * @author liuzhongao
 * @since 2024/6/9 11:38
 */
interface AppMessageDatabase {

    val userSessionId: String

    val sessionContactDao: LocalSessionContactDao

    val messageDao: LocalMessageDao

    suspend fun withTransaction(readOnly: Boolean, block: suspend () -> Unit)

    fun addTableObserver(observer: OnTableChangedObserver)

    fun removeTableObserver(observer: OnTableChangedObserver)

    fun release()

    fun interface Factory {

        fun databaseCreate(sessionId: String): AppMessageDatabase?
    }
}

internal fun AppMessageDatabase(userSessionId: String, database: MessageDatabase): AppMessageDatabase {
    return AppMessageDatabaseImpl(userSessionId, database)
}

private class AppMessageDatabaseImpl(
    override val userSessionId: String,
    private val database: MessageDatabase
) : AppMessageDatabase {

    override val sessionContactDao: LocalSessionContactDao = this.database.sessionDao
    override val messageDao: LocalMessageDao = this.database.messageDao

    private val observerMapping: MutableMap<OnTableChangedObserver, InvalidationTracker.Observer> = ConcurrentHashMap()

    override suspend fun withTransaction(readOnly: Boolean, block: suspend () -> Unit) {
        val function: suspend (Transactor) -> Unit = { transactor ->
            transactor.immediateTransaction {
                block()
            }
        }
        if (readOnly) {
            this.database.useReaderConnection(function)
        } else this.database.useWriterConnection(function)
    }

    override fun addTableObserver(observer: OnTableChangedObserver) {
        if (this.observerMapping.contains(observer)) {
            return
        }
        runBlocking {
            val invalidationTracker = this@AppMessageDatabaseImpl.database.invalidationTracker
            val trackerObserverAdapter = TrackerObserverAdapter(observer)
            invalidationTracker.subscribe(trackerObserverAdapter)
            this@AppMessageDatabaseImpl.observerMapping[observer] = trackerObserverAdapter
        }
    }

    override fun removeTableObserver(observer: OnTableChangedObserver) {
        if (!this.observerMapping.contains(observer)) {
            return
        }
        runBlocking {
            val invalidationTracker = this@AppMessageDatabaseImpl.database.invalidationTracker
            val trackerObserverAdapter =
                this@AppMessageDatabaseImpl.observerMapping.remove(observer) ?: return@runBlocking
            invalidationTracker.unsubscribe(trackerObserverAdapter)
        }
    }

    override fun release() {
        this.database.close()
    }

    class TrackerObserverAdapter(
        private val observer: OnTableChangedObserver
    ) : InvalidationTracker.Observer(tables = observer.tables.toTypedArray()) {
        override fun onInvalidated(tables: Set<String>) {
            this.observer.onTableChanged(tableNames = tables)
        }
    }
}