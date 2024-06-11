package com.application.channel.message

import com.application.channel.database.AppMessageDatabase
import com.application.channel.message.meta.MessageParser

/**
 * @author liuzhongao
 * @since 2024/6/2 10:22
 */
data class ChatServiceInitConfig(
    val remoteAddress: String,
    val messageParserList: List<MessageParser> = emptyList(),
    val messageDatabase: AppMessageDatabase? = null
)
