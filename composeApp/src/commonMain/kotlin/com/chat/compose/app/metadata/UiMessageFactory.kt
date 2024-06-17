package com.chat.compose.app.metadata

import com.application.channel.message.meta.Message
import com.application.channel.message.meta.TextMessage

/**
 * @author liuzhongao
 * @since 2024/6/16 20:52
 */

fun Message.toUiMessage(): UiMessage {
    return when (this) {
        is TextMessage -> TextUiMessage(
            uuid = this.uuid,
            sessionType = this.sessionType,
            textContent = this.content ?: "",
            timestamp = this.timestamp
        )
        else -> throw IllegalArgumentException("illegal message: ${this.javaClass}, message: $this")
    }
}