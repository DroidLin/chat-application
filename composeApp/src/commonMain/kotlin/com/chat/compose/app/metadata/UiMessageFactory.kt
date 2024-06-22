package com.chat.compose.app.metadata

import com.application.channel.message.meta.Message
import com.application.channel.message.meta.TextMessage

/**
 * @author liuzhongao
 * @since 2024/6/16 20:52
 */

fun Message.toUiMessage(
    isSenderMessage: Boolean = false,
    isReceiverMessage: Boolean = false,
): UiMessage {
    return when (this) {
        is TextMessage -> TextUiMessage(
            uuid = this.uuid,
            sessionType = this.sessionType,
            textContent = this.content ?: "",
            isSenderMessage = isSenderMessage,
            isReceiverMessage = isReceiverMessage,
            timestamp = this.timestamp
        )

        else -> throw IllegalArgumentException("illegal message: ${this.javaClass}, message: $this")
    }
}

val Message.showingContent: String?
    get() = when (this) {
        is TextMessage -> this.content
        else -> null
    }