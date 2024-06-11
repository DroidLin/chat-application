package com.application.channel.im

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.application.channel.message.MessageRepository
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import com.application.channel.message.metadata.SessionContact
import kotlinx.coroutines.flow.Flow

/**
 * @author liuzhongao
 * @since 2024/6/10 10:55
 */
fun MsgService(): MsgService {
    return MsgServiceImpl()
}

private class MsgServiceImpl : MsgService {

    private val messageRepository: MessageRepository
        get() = MsgClient.getService(MsgConnectionService::class.java).messageRepository

    override suspend fun insertSessionContact(sessionId: String, sessionType: SessionType) {
        this.messageRepository.insertSessionContact(sessionId, sessionType)
    }

    override suspend fun insertSessionContact(
        sessionId: String,
        sessionType: SessionType,
        function: MutableMap<String, Any?>.() -> Unit
    ) {
        this.messageRepository.updateSessionContact(sessionId, sessionType) {
            val extensions = this.extensions.toMutableMap().apply(function)
            this.copy(extensions = extensions)
        }
    }

    override suspend fun insertSessionContact(sessionContact: SessionContact) {
        this.messageRepository.insertSessionContact(sessionContact)
    }

    override suspend fun updateSessionContact(sessionContact: SessionContact) {
        this.messageRepository.updateSessionContact(
            sessionContact.sessionId,
            sessionContact.sessionType
        ) { sessionContact }
    }

    override suspend fun updateSessionContactExtensions(
        sessionId: String,
        sessionType: SessionType,
        function: MutableMap<String, Any?>.() -> Unit
    ) {
        this.messageRepository.updateSessionContact(sessionId, sessionType) {
            copy(extensions = this.extensions.toMutableMap().apply(function))
        }
    }

    override suspend fun deleteSessionContact(sessionId: String, sessionType: SessionType) {
        this.messageRepository.deleteSessionContact(sessionId, sessionType)
    }

    override suspend fun deleteSessionContact(sessionContact: SessionContact) {
        this.messageRepository.deleteSessionContact(sessionContact)
    }

    override suspend fun fetchRecentSessionContactList(limit: Int): List<SessionContact> {
        return this.messageRepository.fetchRecentSessionContactList(limit)
    }

    override suspend fun fetchObservableSessionContactList(limit: Int): Flow<List<SessionContact>> {
        return this.messageRepository.fetchObservableSessionContactList(limit)
    }

    override suspend fun insertMessage(message: Message) {
        TODO("Not yet implemented")
    }

    override suspend fun updateMessage(message: Message) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMessage(message: Message) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMessage(uuid: String, sessionType: SessionType) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long,
        limit: Int,
        further: Boolean
    ): List<Message> {
        return this.messageRepository.fetchMessages(
            chatterSessionId = chatterSessionId,
            sessionType = sessionType,
            timestamp = timestamp,
            limit = limit,
            further = further,
        )
    }

    override suspend fun fetchObservableMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long,
        limit: Int
    ): Flow<List<Message>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPagedMessages(
        chatterSessionId: String,
        sessionType: SessionType,
        timestamp: Long,
        limit: Int
    ): PagingSource<Long, Message> {
        return object : PagingSource<Long, Message>() {

            private val observer = InvalidationTableObserver(
                tables = listOf(),
                onInvalidation = this::invalidate
            )

            override fun getRefreshKey(state: PagingState<Long, Message>): Long? {
                return timestamp
            }

            override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Message> {
                val messageRepository = this@MsgServiceImpl.messageRepository
                this.observer.registerIfNecessary(messageRepository)
                return when (params) {
                    is LoadParams.Refresh -> {
                        val historyList = messageRepository.fetchMessages(
                            chatterSessionId = chatterSessionId,
                            sessionType = sessionType,
                            timestamp = params.key ?: timestamp,
                            limit = params.loadSize,
                            further = true
                        )
                        LoadResult.Page(
                            data = historyList,
                            prevKey = historyList.firstOrNull()?.timestamp,
                            nextKey = historyList.lastOrNull()?.timestamp
                        )
                    }

                    is LoadParams.Prepend -> {
                        val historyList = messageRepository.fetchMessages(
                            chatterSessionId = chatterSessionId,
                            sessionType = sessionType,
                            timestamp = params.key,
                            limit = params.loadSize,
                            further = false
                        )
                        LoadResult.Page(
                            data = historyList,
                            prevKey = historyList.firstOrNull()?.timestamp,
                            nextKey = null
                        )
                    }

                    is LoadParams.Append -> {
                        val historyList = messageRepository.fetchMessages(
                            chatterSessionId = chatterSessionId,
                            sessionType = sessionType,
                            timestamp = params.key,
                            limit = params.loadSize,
                            further = true
                        )
                        LoadResult.Page(
                            data = historyList,
                            prevKey = null,
                            nextKey = historyList.lastOrNull()?.timestamp
                        )
                    }

                    else -> LoadResult.Error(Throwable("invalid load params: ${params.javaClass.name}"))
                }
            }
        }
    }

    override suspend fun markMessageAsRead(message: Message) {
        TODO("Not yet implemented")
    }

    override suspend fun markMessageAsRead(uuid: String, sessionType: SessionType) {
        TODO("Not yet implemented")
    }

    override suspend fun clearUnreadCount(sessionId: String, sessionType: SessionType) {
        TODO("Not yet implemented")
    }
}