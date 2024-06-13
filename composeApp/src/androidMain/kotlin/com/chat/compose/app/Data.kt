package com.chat.compose.app

import com.application.channel.message.Account
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Messages

/**
 * @author liuzhongao
 * @since 2024/6/13 00:39
 */
val textMessageContent = listOf(
    "Hello World.",
    "Ok",
    "Fine.",
    "Check it out!",
    "John Glenn",
    "Taylor Brooks",
    "You can use all the same stuff",
    "8:05 PM",
    "@aliconors Take a look at the `Flow.collectAsStateWithLifecycle()` APIs",
    "8:05 PM",
    "Check it out!",
    "8:07 PM",
    "Hello World.",
    "Ok",
    "Fine.",
    "Check it out!",
    "John Glenn",
    "Taylor Brooks",
    "You can use all the same stuff",
    "8:05 PM",
    "@aliconors Take a look at the `Flow.collectAsStateWithLifecycle()` APIs",
    "8:05 PM",
    "Check it out!",
    "8:07 PM",
    "Hello World.",
    "Ok",
    "Fine.",
    "Check it out!",
    "John Glenn",
    "Taylor Brooks",
    "You can use all the same stuff",
    "8:05 PM",
    "@aliconors Take a look at the `Flow.collectAsStateWithLifecycle()` APIs",
    "8:05 PM",
    "Check it out!",
    "8:07 PM",
    "Hello World.",
    "Ok",
    "Fine.",
    "Check it out!",
    "John Glenn",
    "Taylor Brooks",
    "You can use all the same stuff",
    "8:05 PM",
    "@aliconors Take a look at the `Flow.collectAsStateWithLifecycle()` APIs",
    "8:05 PM",
    "Check it out!",
    "8:07 PM",
    "Hello World.",
    "Ok",
    "Fine.",
    "Check it out!",
    "John Glenn",
    "Taylor Brooks",
    "You can use all the same stuff",
    "8:05 PM",
    "@aliconors Take a look at the `Flow.collectAsStateWithLifecycle()` APIs",
    "8:05 PM",
    "Check it out!",
    "8:07 PM",
)

val messages = textMessageContent.map {
    Messages.buildTextMessage(
        content = it,
        sessionType = SessionType.P2P
    ).also { textMessage ->
        textMessage.sender = Account(sessionId = "111111", accountId = "111111")
        textMessage.receiver = Account(sessionId = "222222", accountId = "222222")
    }
}