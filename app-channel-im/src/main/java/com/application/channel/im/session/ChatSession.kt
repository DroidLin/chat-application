package com.application.channel.im.session

import androidx.paging.PagingSource
import com.application.channel.message.MessageReceiveListener
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message

/**
 * @author liuzhongao
 * @since 2024/6/9 20:48
 */
interface ChatSession {

    val sessionId: String

    val sessionType: SessionType

    fun addListener(listener: MessageReceiveListener)

    fun removeListener(listener: MessageReceiveListener)

    suspend fun fetchHistoryMessages(timestamp: Long = Long.MAX_VALUE, limit: Int = 20): List<Message>

    /**
     * fetch message history bundled with paging-source.
     */
    suspend fun historyMessageSource(timestamp: Long = Long.MAX_VALUE, limit: Int = 20): PagingSource<Long, Message>

    suspend fun saveDraftContent(content: String)

    fun close()
}