package com.application.channel.im

import androidx.paging.PagingSource
import com.application.channel.database.AppMessageDatabase.Transaction
import com.application.channel.database.OnTableChangedObserver
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import com.application.channel.message.metadata.SessionContact
import kotlinx.coroutines.flow.Flow

/**
 * @author liuzhongao
 * @since 2024/6/10 09:53
 */
interface MsgService {

    suspend fun <T> withTransaction(readOnly: Boolean, block: suspend Transaction<T>.(MsgService) -> T): T

    // SessionContact
    suspend fun insertSessionContact(sessionId: String, sessionType: SessionType)
    suspend fun insertSessionContact(
        sessionId: String,
        sessionType: SessionType,
        function: MutableMap<String, Any?>.() -> Unit
    )

    suspend fun insertSessionContact(sessionContact: SessionContact)

    suspend fun updateSessionContact(sessionContact: SessionContact)
    suspend fun updateSessionContactExtensions(
        sessionId: String,
        sessionType: SessionType,
        function: MutableMap<String, Any?>.() -> Unit
    )
    suspend fun updateSessionContactExtensions(
        sessionId: String,
        sessionType: SessionType,
        updateAccess: Boolean,
        function: MutableMap<String, Any?>.() -> Unit
    )

    suspend fun fetchSessionContact(sessionId: String, sessionType: SessionType): SessionContact?
    fun fetchObservableSessionContact(sessionId: String, sessionType: SessionType): Flow<SessionContact?>

    suspend fun deleteSessionContact(sessionId: String, sessionType: SessionType)
    suspend fun deleteSessionContact(sessionContact: SessionContact)

    suspend fun fetchRecentSessionContactList(limit: Int): List<SessionContact>
    fun fetchObservableRecentSessionContactList(limit: Int): Flow<List<SessionContact>>

    // Messages
    suspend fun insertMessage(message: Message)
    suspend fun insertMessages(messageList: List<Message>)
    suspend fun updateMessage(message: Message)
    suspend fun deleteMessage(message: Message)
    suspend fun deleteMessage(uuid: String, sessionType: SessionType)

    suspend fun fetchMessagesAtTime(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long = Long.MAX_VALUE,
    ): List<Message>

    suspend fun fetchMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long = Long.MAX_VALUE,
        limit: Int = 20,
        further: Boolean = true,
    ): List<Message>

    suspend fun fetchObservableMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long = Long.MAX_VALUE,
        limit: Int = 20
    ): Flow<List<Message>>

    fun fetchPagedMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long = Long.MAX_VALUE,
        limit: Int = 20
    ): PagingSource<Long, Message>

    fun fetchPagedMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        anchor: Message?,
    ): PagingSource<Message, Message>

    // UnreadCount
    suspend fun markMessageAsRead(message: Message)
    suspend fun markMessageAsRead(uuid: String, sessionType: SessionType)
    suspend fun clearUnreadCount(sessionId: String, sessionType: SessionType)


    fun addObserver(observer: OnTableChangedObserver)
    fun removeObserver(observer: OnTableChangedObserver)

}