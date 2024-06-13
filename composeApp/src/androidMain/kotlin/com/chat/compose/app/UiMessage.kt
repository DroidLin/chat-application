package com.chat.compose.app

import androidx.compose.runtime.Immutable
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import com.application.channel.message.meta.TextMessage

/**
 * @author liuzhongao
 * @since 2024/6/13 01:04
 */
@Immutable
interface UiMessage {

    val uuid: String

    val sessionType: SessionType

    val timestamp: Long
}

@Immutable
data class UiTextMessage(
    override val uuid: String,
    override val sessionType: SessionType,
    override val timestamp: Long,
    val textContent: String
) : UiMessage

fun convertMessageToUiMessage(message: Message): UiMessage {
    return when (message) {
        is TextMessage -> UiTextMessage(
            uuid = message.uuid,
            sessionType = message.sessionType,
            textContent = message.content ?: "",
            timestamp = message.timestamp
        )
        else -> throw IllegalArgumentException("illegal message: ${message.javaClass}, message: $message")
    }
}