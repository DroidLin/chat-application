package com.application.channel.im

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.application.channel.database.LocalMessageTable
import com.application.channel.message.MessageRepository
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import kotlinx.coroutines.delay

/**
 * @author liuzhongao
 * @since 2024/6/14 21:56
 */
class MessagePagingSource(
    private val chatterSessionId: String,
    private val sessionType: SessionType,
    private val anchor: Message?,
    private val messageRepository: MessageRepository,
) : PagingSource<Message, Message>() {

    private val observer = InvalidationTableObserver(
        tables = listOf(LocalMessageTable.TABLE_NAME),
        onInvalidation = this::invalidate
    )

    init {
        this.registerInvalidatedCallback {
            this.observer.unregisterIfNecessary(this.messageRepository)
        }
    }

    override fun getRefreshKey(state: PagingState<Message, Message>): Message? {
        val anchorPosition = state.anchorPosition ?: return this.anchor
        val pageList = state.pages.flatMap { it.data }
        return pageList.getOrNull(anchorPosition) ?: return this.anchor
    }

    override suspend fun load(params: LoadParams<Message>): LoadResult<Message, Message> {
        this.observer.registerIfNecessary(this.messageRepository)
        return when (params) {
            is LoadParams.Refresh -> {
                val backwardHistoryList = this.messageRepository.fetchMessagesWithId(
                    chatterSessionId = this.chatterSessionId,
                    sessionType = this.sessionType,
                    anchor = params.key ?: anchor,
                    limit = params.loadSize,
                    further = false
                )
                val middleHistoryList = this.messageRepository.fetchMessagesAtTime(
                    chatterSessionId = this.chatterSessionId,
                    sessionType = this.sessionType,
                    anchor = params.key ?: anchor,
                )
                val furtherHistoryList = this.messageRepository.fetchMessagesWithId(
                    chatterSessionId = this.chatterSessionId,
                    sessionType = this.sessionType,
                    anchor = params.key ?: anchor,
                    limit = params.loadSize,
                    further = true
                )
                LoadResult.Page(
                    data = backwardHistoryList + middleHistoryList + furtherHistoryList,
                    prevKey = backwardHistoryList.firstOrNull(),
                    nextKey = furtherHistoryList.lastOrNull()
                )
            }

            is LoadParams.Prepend -> {
                val historyList = this.messageRepository.fetchMessagesWithId(
                    chatterSessionId = this.chatterSessionId,
                    sessionType = this.sessionType,
                    anchor = params.key,
                    limit = params.loadSize,
                    further = false
                )
                LoadResult.Page(
                    data = historyList,
                    prevKey = historyList.firstOrNull(),
                    nextKey = null
                )
            }

            is LoadParams.Append -> {
                val historyList = this.messageRepository.fetchMessagesWithId(
                    chatterSessionId = this.chatterSessionId,
                    sessionType = this.sessionType,
                    anchor = params.key,
                    limit = params.loadSize,
                    further = true
                )
                LoadResult.Page(
                    data = historyList,
                    prevKey = null,
                    nextKey = historyList.lastOrNull()
                )
            }

            else -> LoadResult.Error(Throwable("invalid load params: ${params.javaClass}"))
        }
    }
}