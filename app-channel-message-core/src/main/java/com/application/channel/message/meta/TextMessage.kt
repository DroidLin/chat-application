package com.application.channel.message.meta

import com.application.channel.message.Account
import com.application.channel.message.MsgType
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Messages.decodeToBasicMessage
import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/5/30 00:41
 */
data class TextMessage internal constructor(
    override val uuid: String,
    override val sessionType: SessionType,
    override var sender: Account,
    override var receiver: Account,
    override val timestamp: Long,
    override val ext: Map<String, Any?>
) : Message {

    constructor(message: Message) : this(
        uuid = message.uuid,
        sessionType = message.sessionType,
        sender = message.sender,
        receiver = message.receiver,
        timestamp = message.timestamp,
        ext = message.ext
    )

    override val msgType: MsgType = MsgType.Text

    val content: String?
        get() = this.ext[KEY_CONTENT] as? String

    class Parser : MessageParser {
        override fun parse(jsonObject: JSONObject): Message? {
            val basicMessage = jsonObject.decodeToBasicMessage() ?: return null
            return this.parse(basicMessage)
        }

        override fun parse(message: Message): Message? {
            if (message.msgType != MsgType.Image) return null
            return TextMessage(message)
        }
    }
}