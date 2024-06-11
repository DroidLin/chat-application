package com.application.channel.message.meta

import com.application.channel.message.Account
import com.application.channel.message.MsgType
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Messages.decodeToBasicMessage
import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/6/1 19:52
 */
data class LoginResultMessage internal constructor(
    override val uuid: String,
    override val sender: Account,
    override val receiver: Account,
    override val timestamp: Long,
    override val ext: Map<String, Any?>
) : Message {

    override val sessionType: SessionType get() = SessionType.Unknown

    constructor(message: Message) : this(
        uuid = message.uuid,
        sender = message.sender,
        receiver = message.receiver,
        timestamp = message.timestamp,
        ext = message.ext
    )

    override val msgType: MsgType = MsgType.Custom

    val authorized: Boolean by lazy {
        (this.ext[KEY_CONTENT] as? Map<*, *>)?.get(KEY_AUTHORIZED) as? Boolean ?: false
    }

    val message: String? by lazy {
        (this.ext[KEY_CONTENT] as? Map<*, *>)?.get(KEY_MESSAGE) as? String
    }

    class Parser : MessageParser {
        override fun parse(jsonObject: JSONObject): Message? {
            val basicMessage = jsonObject.decodeToBasicMessage() ?: return null
            return this.parse(basicMessage)
        }

        override fun parse(message: Message): Message? {
            if (message.msgType != MsgType.Custom) return null

            val customType = message.ext[KEY_TYPE] as? String ?: return null
            if (customType != TYPE_LOGIN_RESULT_MESSAGE) return null
            return LoginResultMessage(message)
        }
    }
}
