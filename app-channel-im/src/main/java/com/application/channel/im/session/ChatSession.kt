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

    val context: ChatSessionContext

    fun addListener(listener: MessageReceiveListener)

    fun removeListener(listener: MessageReceiveListener)

    fun sendTextMessage(text: String)

    suspend fun fetchHistoryMessages(timestamp: Long = Long.MAX_VALUE, limit: Int = 20): List<Message>

    /**
     * fetch message history bundled with paging-source.
     */
    fun historyMessageSource(timestamp: Long = Long.MAX_VALUE, limit: Int = 20): PagingSource<Long, Message>

    /**
     * fetch message history bundled with paging-source.
     */
    fun historyMessageSource(anchorMessage: Message?, limit: Int = 20): PagingSource<Message, Message>

    suspend fun saveDraftContent(content: String)

    fun close()
}