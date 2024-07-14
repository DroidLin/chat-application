package com.application.channel.message

import com.application.channel.message.database.DBProvider
import com.application.channel.message.meta.LoginMessage
import com.application.channel.message.meta.LoginResultMessage
import com.application.channel.message.meta.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author liuzhongao
 * @since 2024/6/8 21:17
 */
class MessagePersistenceListenerAdapter(
    private val databaseProvider: DBProvider?,
    private val coroutineContext: CoroutineContext = EmptyCoroutineContext
) : MessageReceiveListener {

    private val coroutineMutex = Mutex(false)
    private val coroutineScope = CoroutineScope(
        context = if (this.coroutineContext == EmptyCoroutineContext) {
            Dispatchers.Default
        } else this.coroutineContext
    )

    override fun onReceive(message: Message) {
        val databaseProvider = this.databaseProvider ?: return
        // skip persist login related messages
        if (!databaseProvider.isDatabaseAvailable || message is LoginMessage || message is LoginResultMessage) return

        // always pick up session id related to target but not mine.
        val sessionId = if (message.sender.sessionId == databaseProvider.userSessionId) {
            message.receiver.sessionId
        } else if (message.receiver.sessionId == databaseProvider.userSessionId) {
            message.sender.sessionId
        } else return

        val sessionContactDao = databaseProvider.sessionContactDatabaseApi
        val messageApi = databaseProvider.messageDatabaseApi
        this.coroutineScope.launch {
            this@MessagePersistenceListenerAdapter.coroutineMutex.withLock {
                sessionContactDao.accessToSessionContact(sessionId, message.sessionType)
                messageApi.insertMessage(message)
            }
        }
    }
}