package com.application.channel.message.meta

import com.application.channel.message.Account
import com.application.channel.message.MsgType
import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/5/6 23:22
 */
interface Message {

    val uuid: String

    val msgType: MsgType

    val sessionType: SessionType

    val sender: Account

    val receiver: Account

    val timestamp: Long

    val ext: Map<String, Any?>
}