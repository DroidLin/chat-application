package com.application.channel.database

import androidx.room.*
import com.application.channel.database.dao.LocalMessageDao
import com.application.channel.database.dao.LocalSessionContactDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

/**
 * @author liuzhongao
 * @since 2024/6/9 11:38
 */
interface AppMessageDatabase {

    val userSessionId: String

    val sessionContactDao: LocalSessionContactDao

    val messageDao: LocalMessageDao

    suspend fun <T> withTransaction(readOnly: Boolean, block: suspend Transaction<T>.() -> T): T

    fun addTableObserver(observer: OnTableChangedObserver)

    fun removeTableObserver(observer: OnTableChangedObserver)

    fun release()

    interface Transaction<T> {
        suspend fun rollback(result: T)
    }

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

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override val sessionContactDao: LocalSessionContactDao = this.database.sessionDao
    override val messageDao: LocalMessageDao = this.database.messageDao
    private val observerMapping: MutableMap<OnTableChangedObserver, InvalidationTracker.Observer> = ConcurrentHashMap()

    override suspend fun <T> withTransaction(
        readOnly: Boolean,
        block: suspend AppMessageDatabase.Transaction<T>.() -> T
    ): T {
        val function: suspend (Transactor) -> T = { transactor ->
            if (transactor.inTransaction()) {
                EmptyTransaction<T>().block()
            } else transactor.deferredTransaction {
                TransactionScopedTransaction<T>(this).block()
            }
        }
        return if (readOnly) {
            this.database.useReaderConnection(function)
        } else this.database.useWriterConnection(function)
    }

    override fun addTableObserver(observer: OnTableChangedObserver) {
        if (this.observerMapping.contains(observer)) {
            return
        }
        this.coroutineScope.launch {
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
        this.coroutineScope.launch {
            val invalidationTracker = this@AppMessageDatabaseImpl.database.invalidationTracker
            val trackerObserverAdapter = this@AppMessageDatabaseImpl.observerMapping.remove(observer)
            if (trackerObserverAdapter != null) {
                invalidationTracker.unsubscribe(trackerObserverAdapter)
            }
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

    private class EmptyTransaction<T>: AppMessageDatabase.Transaction<T> {
        override suspend fun rollback(result: T) {
        }
    }

    private class TransactionScopedTransaction<T>(private val transactionScope: TransactionScope<T>): AppMessageDatabase.Transaction<T> {
        override suspend fun rollback(result: T) {
            this.transactionScope.rollback(result)
        }
    }
}