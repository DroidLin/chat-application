package com.application.channel.message.meta

import com.application.channel.message.Account
import com.application.channel.message.MsgType
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Messages.decodeToBasicMessage
import org.json.JSONObject

internal data class MessageImpl(
    override val uuid: String,
    override val msgType: MsgType,
    override val sessionType: SessionType,
    override val sender: Account,
    override val receiver: Account,
    override val timestamp: Long,
    override val ext: Map<String, Any?>
) : Message {

    class Parser : MessageParser {
        override fun parse(jsonObject: JSONObject): Message? = jsonObject.decodeToBasicMessage()
        override fun parse(message: Message): Message? = message
    }
}