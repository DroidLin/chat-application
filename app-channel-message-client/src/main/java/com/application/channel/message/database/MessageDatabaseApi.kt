package com.application.channel.message.database

import com.application.channel.database.AppMessageDatabase
import com.application.channel.database.meta.LocalMessage
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

    suspend fun queryMessagesAtTime(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long
    ): List<Message>

    suspend fun queryMessagesAtTime(
        chatterSessionId: String,
        sessionType: SessionType,
        anchor: Message?
    ): List<Message>

    suspend fun queryMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long,
        limit: Int,
        further: Boolean
    ): List<Message>

    suspend fun queryMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        limit: Int,
        further: Boolean,
        anchor: Message? = null
    ): List<Message>

    suspend fun insertMessage(message: Message)
    suspend fun insertMessage(messageList: List<Message>)

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

    override suspend fun queryMessagesAtTime(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long
    ): List<Message> {
        val database = this.database ?: return emptyList()
        val localMessages = database.messageDao.fetchMessagesAtTime(
            sessionId = database.userSessionId,
            chatterSessionId = chatterSessionId,
            sessionTypeCode = sessionType.value,
            timestamp = timestamp
        )
        return localMessages.toMessageList()
    }

    override suspend fun queryMessagesAtTime(
        chatterSessionId: String,
        sessionType: SessionType,
        anchor: Message?
    ): List<Message> {
        val database = this.database ?: return emptyList()
        val localMessage = if (anchor != null) {
            database.messageDao.fetchMessageByUUid(uuid = anchor.uuid, sessionTypeCode = sessionType.value)
        } else null

        val id = localMessage?.id ?: Int.MAX_VALUE
        val localMessages = database.messageDao.fetchMessagesAtTimeWithId(
            sessionId = database.userSessionId,
            chatterSessionId = chatterSessionId,
            sessionTypeCode = sessionType.value,
            id = id
        )
        return localMessages.toMessageList()
    }

    override suspend fun queryMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long,
        limit: Int,
        further: Boolean
    ): List<Message> {
        val database = this.database ?: return emptyList()
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
        return localMessages.toMessageList()
    }

    override suspend fun queryMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        limit: Int,
        further: Boolean,
        anchor: Message?
    ): List<Message> {
        val database = this.database ?: return emptyList()
        val localMessage = if (anchor != null) {
            database.messageDao.fetchMessageByUUid(uuid = anchor.uuid, sessionTypeCode = sessionType.value)
        } else null

        val timestamp = localMessage?.timestamp ?: Long.MAX_VALUE
        val id = localMessage?.id ?: Int.MAX_VALUE

        val localMessages = if (further) {
            database.messageDao.fetchMessagesWithId(
                sessionId = database.userSessionId,
                chatterSessionId = chatterSessionId,
                sessionTypeCode = sessionType.value,
                id = id,
                timestamp = timestamp,
                limit = limit
            )
        } else database.messageDao.fetchMessagesDescWithId(
            sessionId = database.userSessionId,
            chatterSessionId = chatterSessionId,
            sessionTypeCode = sessionType.value,
            id = id,
            timestamp = timestamp,
            limit = limit
        )
        return localMessages.toMessageList()
    }

    private suspend fun List<LocalMessage>.toMessageList(): List<Message> {
        return coroutineScope {
            this@toMessageList.map { localMessage ->
                async {
                    val message = localMessage.toMessage()
                    if (message != null) {
                        this@MessageDatabaseApiImpl.messageParser.parse(message)
                    } else null
                }
            }.awaitAll().filterNotNull()
        }
    }

    override suspend fun insertMessage(message: Message) {
        val database = this.database ?: return
        val messageInDatabase = database.messageDao.fetchMessageByUUid(message.uuid, message.sessionType.value)
        val newLocalMessage = message.toLocalMessage(messageInDatabase?.id ?: 0)
        database.messageDao.upsertMessage(newLocalMessage)
    }

    override suspend fun insertMessage(messageList: List<Message>) {
        val database = this.database ?: return
        messageList.groupBy { it.sessionType }.forEach { (sessionType, messageList) ->
            val uuidList = messageList.map { it.uuid }
            val messagesInDatabase = database.messageDao.fetchMessages(uuidList, sessionType.value)
            val localMessages = messageList.map { message ->
                val messageInDatabase = messagesInDatabase.find { localMessage -> localMessage.uuid == message.uuid }
                message.toLocalMessage(messageInDatabase?.id ?: 0)
            }
            database.messageDao.upsertMessages(localMessages)
        }
    }

    override suspend fun deleteMessage(uuid: String, sessionType: SessionType) {
        val database = this.database ?: return
        val message = database.messageDao.fetchMessageByUUid(uuid, sessionType.value)
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
        val message = database.messageDao.fetchMessageByUUid(uuid, sessionType.value) ?: return
        database.messageDao.updateMessage(message.copy(stateUserConsumed = true))
    }
}