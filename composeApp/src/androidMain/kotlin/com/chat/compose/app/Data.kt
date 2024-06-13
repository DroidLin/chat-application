package com.chat.compose.app

import com.application.channel.message.Account
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Messages

/**
 * @author liuzhongao
 * @since 2024/6/13 00:39
 */
val textMessageContent = mutableListOf<String>().also { list ->
    repeat(100) { index ->
        list += "text message content: $index"
    }
}

val messages = textMessageContent.mapIndexed { index, s ->
    Messages.buildTextMessage(
        content = s,
        sessionType = SessionType.P2P,
        timestamp = index.toLong(),
    ).also { textMessage ->
        textMessage.sender = Account(sessionId = "111111", accountId = "111111")
        textMessage.receiver = Account(sessionId = "222222", accountId = "222222")
    }
}