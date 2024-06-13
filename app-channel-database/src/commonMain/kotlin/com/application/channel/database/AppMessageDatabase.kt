package com.application.channel.database

import androidx.room.InvalidationTracker
import androidx.room.immediateTransaction
import androidx.room.useReaderConnection
import androidx.room.useWriterConnection
import com.application.channel.database.dao.LocalMessageDao
import com.application.channel.database.dao.LocalSessionContactDao
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.ref.WeakReference

/**
 * @author liuzhongao
 * @since 2024/6/9 11:38
 */
interface AppMessageDatabase {

    val userSessionId: String

    val sessionContactDao: LocalSessionContactDao

    val messageDao: LocalMessageDao

    suspend fun withTransaction(block: suspend () -> Unit)

    fun addTableObserver(observer: OnTableChangedObserver)

    fun interface Factory {

        fun databaseCreate(sessionId: String): AppMessageDatabase
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

    private val mutex = Mutex()

    override suspend fun withTransaction(block: suspend () -> Unit) {
        this.database.useWriterConnection<Unit> { transactor ->
            transactor.immediateTransaction {
                block()
            }
        }
    }

    override fun addTableObserver(observer: OnTableChangedObserver) {
        runBlocking {
            val invalidationTracker = this@AppMessageDatabaseImpl.database.invalidationTracker
            val weakObserver = WeakObserver(invalidationTracker, this@AppMessageDatabaseImpl.mutex, observer)
            invalidationTracker.subscribe(weakObserver)
        }
    }

    class WeakObserver(
        private val invalidationTracker: InvalidationTracker,
        private val mutex: Mutex,
        observer: OnTableChangedObserver
    ) : InvalidationTracker.Observer(tables = observer.tables.toTypedArray()) {
        private val weakReference = WeakReference(observer)
        override fun onInvalidated(tables: Set<String>) {
            val observer = this.weakReference.get()
            if (observer == null) {
                runBlocking {
                    this@WeakObserver.invalidationTracker.unsubscribe(this@WeakObserver)
                }
            } else observer.onTableChanged(tableNames = tables)
        }
    }
}