package com.application.channel.im

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.application.channel.database.LocalMessageTable
import com.application.channel.message.MessageRepository
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message

/**
 * @author liuzhongao
 * @since 2024/6/12 23:08
 */
class MessageLimitTimestampPagingSource(
    private val chatterSessionId: String,
    private val sessionType: SessionType,
    private val timestamp: Long,
    private val limit: Int,
    private val messageRepository: MessageRepository,
) : PagingSource<Long, Message>() {

    private val observer = InvalidationTableObserver(
        tables = listOf(LocalMessageTable.TABLE_NAME),
        onInvalidation = this::invalidate
    )

    override fun getRefreshKey(state: PagingState<Long, Message>): Long? {
        return this.timestamp
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Message> {
        this.observer.registerIfNecessary(this.messageRepository)
        return when (params) {
            is LoadParams.Refresh -> {
                val historyList = this.messageRepository.fetchMessages(
                    chatterSessionId = this.chatterSessionId,
                    sessionType = this.sessionType,
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
                val historyList = this.messageRepository.fetchMessages(
                    chatterSessionId = this.chatterSessionId,
                    sessionType = this.sessionType,
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
                val historyList = this.messageRepository.fetchMessages(
                    chatterSessionId = this.chatterSessionId,
                    sessionType = this.sessionType,
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