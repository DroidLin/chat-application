package com.application.channel.message

import com.application.channel.core.model.Writable
import com.application.channel.message.meta.Message

/**
 * @author liuzhongao
 * @since 2024/6/1 11:19
 */
data class MessageWritable(val message: Message) : Writable