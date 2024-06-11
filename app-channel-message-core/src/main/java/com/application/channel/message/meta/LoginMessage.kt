package com.application.channel.message.meta

import com.application.channel.message.Account
import com.application.channel.message.MsgType
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Messages.decodeToBasicMessage
import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/5/31 00:12
 */
data class LoginMessage internal constructor(
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

    override val msgType: MsgType get() = MsgType.Custom

    private val loginInfo: Account by lazy {
        val content = this.ext[KEY_CONTENT] as? Map<*, *>
        val sessionId = content?.get(KEY_SESSION_ID) as? String ?: ""
        val accountId = content?.get(KEY_ACCOUNT_ID) as? String ?: ""
        Account(sessionId, accountId)
    }

    val sessionId: String get() = this.loginInfo.sessionId
    val accountId: String get() = this.loginInfo.accountId

    class Parser : MessageParser {
        override fun parse(jsonObject: JSONObject): Message? {
            val basicMessage = jsonObject.decodeToBasicMessage() ?: return null
            return this.parse(basicMessage)
        }

        override fun parse(message: Message): Message? {
            if (message.msgType != MsgType.Custom) return null

            val customType = message.ext[KEY_TYPE] as? String ?: return null
            if (customType != TYPE_LOGIN_MESSAGE) return null
            return LoginMessage(message)
        }
    }
}