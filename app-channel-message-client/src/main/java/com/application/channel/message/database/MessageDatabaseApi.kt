package com.application.channel.message.database

import com.application.channel.database.AppMessageDatabase
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import com.application.channel.message.meta.MessageParser
import com.application.channel.message.util.LocalMessageConverter.toLocalMessage
import com.application.channel.message.util.LocalMessageConverter.toMessage
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * @author liuzhongao
 * @since 2024/6/9 11:17
 */
interface MessageDatabaseApi {

    suspend fun persistMessage(message: Message)

    suspend fun queryMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long,
        limit: Int,
        further: Boolean
    ): List<Message>

    suspend fun insertMessage(message: Message)

    suspend fun deleteMessage(uuid: String, sessionType: SessionType)
    suspend fun deleteMessagesAboutSomeone(chatterSessionId: String, sessionType: SessionType)

    suspend fun markMessageAsRead(uuid: String, sessionType: SessionType)
}

internal fun MessageDatabaseApi(database: AppMessageDatabase?, messageParser: MessageParser): MessageDatabaseApi {
    return MessageDatabaseApiImpl(database, messageParser)
}

private class MessageDatabaseApiImpl(
    private val database: AppMessageDatabase?,
    private val messageParser: MessageParser,
) : MessageDatabaseApi {

    override suspend fun persistMessage(message: Message) {
        val database = this.database ?: return
        val localMessage = message.toLocalMessage()
        database.messageDao.upsertMessage(localMessage)
    }

    override suspend fun queryMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long,
        limit: Int,
        further: Boolean
    ): List<Message> {
        val database = this.database ?: return emptyList()
        val messageParser = this.messageParser

        val localMessages = if (further) {
            database.messageDao.fetchMessages(
                sessionId = database.userSessionId,
                chatterSessionId = chatterSessionId,
                sessionTypeCode = sessionType.value,
                timestamp = timestamp,
                limit = limit
            )
        } else database.messageDao.fetchMessagesDesc(
            sessionId = database.userSessionId,
            chatterSessionId = chatterSessionId,
            sessionTypeCode = sessionType.value,
            timestamp = timestamp,
            limit = limit
        )
        return coroutineScope {
            localMessages.map { localMessage ->
                async {
                    val message = localMessage.toMessage()
                    if (message != null) {
                        messageParser.parse(message)
                    } else null
                }
            }.awaitAll().filterNotNull()
        }
    }

    override suspend fun insertMessage(message: Message) {
        val database = this.database ?: return
        database.messageDao.upsertMessage(message.toLocalMessage())
    }

    override suspend fun deleteMessage(uuid: String, sessionType: SessionType) {
        val database = this.database ?: return
        val message = database.messageDao.fetchMessageById(uuid, sessionType.value)
        if (message != null) {
            database.messageDao.deleteMessages(message)
        }
    }

    override suspend fun deleteMessagesAboutSomeone(chatterSessionId: String, sessionType: SessionType) {
        val database = this.database ?: return
        val allMessages = database.messageDao.fetchMessages(
            sessionId = database.userSessionId,
            chatterSessionId = chatterSessionId,
            sessionTypeCode = sessionType.value,
            timestamp = Long.MAX_VALUE,
            limit = Int.MAX_VALUE
        )
        database.messageDao.deleteMessages(allMessages)
    }

    override suspend fun markMessageAsRead(uuid: String, sessionType: SessionType) {
        val database = this.database ?: return
        database.withTransaction {
            val message = database.messageDao.fetchMessageById(uuid, sessionType.value) ?: return@withTransaction
            database.messageDao.updateMessage(message.copy(stateUserConsumed = true))
        }
    }
}