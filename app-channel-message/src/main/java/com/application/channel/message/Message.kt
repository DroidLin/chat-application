package com.application.channel.message

/**
 * @author liuzhongao
 * @since 2024/5/6 23:22
 */
interface Message {

    val msgType: MsgType

    val sender: Account

    val receiver: Account

    val timestamp: Long

    val ext: Map<String, Any?>
}