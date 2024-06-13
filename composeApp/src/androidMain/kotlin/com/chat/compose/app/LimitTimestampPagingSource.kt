package com.chat.compose.app

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.application.channel.database.LocalMessageTable
import com.application.channel.im.MsgService
import com.application.channel.message.SessionType

class LimitTimestampPagingSource(
    private val chatterSessionId: String,
    private val sessionType: SessionType,
    private val timestamp: Long,
    private val limit: Int,
    private val msgService: MsgService,
) : PagingSource<Long, UiMessage>() {

    private val observer = InvalidationTableObserver(
        tables = listOf(LocalMessageTable.TABLE_NAME),
        onInvalidation = this::invalidate
    )

    init {
        this.registerInvalidatedCallback {

        }
    }

    override fun getRefreshKey(state: PagingState<Long, UiMessage>): Long? {
        return this.timestamp
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, UiMessage> {
        this.observer.registerIfNecessary(this.msgService)
        return when (params) {
            is LoadParams.Refresh -> {
                val historyList = this.msgService.fetchMessages(
                    chatterSessionId = this.chatterSessionId,
                    sessionType = this.sessionType,
                    timestamp = params.key ?: timestamp,
                    limit = params.loadSize,
                    further = true
                ).map(::convertMessageToUiMessage)
                LoadResult.Page(
                    data = historyList,
                    prevKey = historyList.firstOrNull()?.timestamp,
                    nextKey = historyList.lastOrNull()?.timestamp
                )
            }

            is LoadParams.Prepend -> {
                val historyList = this.msgService.fetchMessages(
                    chatterSessionId = this.chatterSessionId,
                    sessionType = this.sessionType,
                    timestamp = params.key,
                    limit = params.loadSize,
                    further = false
                ).map(::convertMessageToUiMessage)
                LoadResult.Page(
                    data = historyList,
                    prevKey = historyList.firstOrNull()?.timestamp,
                    nextKey = null
                )
            }

            is LoadParams.Append -> {
                val historyList = this.msgService.fetchMessages(
                    chatterSessionId = this.chatterSessionId,
                    sessionType = this.sessionType,
                    timestamp = params.key,
                    limit = params.loadSize,
                    further = true
                ).map(::convertMessageToUiMessage)
                LoadResult.Page(
                    data = historyList,
                    prevKey = null,
                    nextKey = historyList.lastOrNull()?.timestamp
                )
            }

            else -> LoadResult.Error(Throwable("invalid load params: ${params.javaClass}"))
        }
    }
}