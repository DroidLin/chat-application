package com.application.channel.message.meta

import com.application.channel.message.Account
import com.application.channel.message.MsgType
import com.application.channel.message.SessionType
import com.application.channel.message.map
import org.json.JSONObject
import java.util.UUID

/**
 * @author liuzhongao
 * @since 2024/5/30 00:43
 */

object Messages {

    @JvmStatic
    fun buildTextMessage(
        content: String,
        selfSessionId: String,
        sessionId: String,
        sessionType: SessionType
    ): TextMessage {
        val extensions = mapOf(KEY_CONTENT to content)
        val textMessage = TextMessage(
            uuid = UUID.randomUUID().toString(),
            sessionType = sessionType,
            sender = Account(sessionId = selfSessionId, accountId = selfSessionId),
            receiver = Account(sessionId = sessionId, accountId = sessionId),
            timestamp = System.currentTimeMillis(),
            ext = extensions
        )
        return textMessage
    }

    @JvmStatic
    fun buildTextMessage(content: String, sessionType: SessionType, timestamp: Long): TextMessage {
        val extensions = mapOf(KEY_CONTENT to content)
        val textMessage = TextMessage(
            uuid = UUID.randomUUID().toString(),
            sessionType = sessionType,
            sender = Account.default,
            receiver = Account.default,
            timestamp = timestamp,
            ext = extensions
        )
        return textMessage
    }

    @JvmStatic
    fun buildLoginMessage(sessionId: String, accountId: String): LoginMessage {
        val extensions = mapOf(
            KEY_CONTENT to mapOf(
                KEY_SESSION_ID to sessionId,
                KEY_ACCOUNT_ID to accountId
            ),
            KEY_TYPE to TYPE_LOGIN_MESSAGE
        )
        return LoginMessage(
            uuid = UUID.randomUUID().toString(),
            sender = Account(sessionId, accountId),
            receiver = Account(sessionId, accountId),
            timestamp = System.currentTimeMillis(),
            ext = extensions
        )
    }

    @JvmStatic
    fun buildLoginResultMessage(account: Account, authorized: Boolean, messages: String? = null): LoginResultMessage {
        val extensions = mapOf(
            KEY_CONTENT to mapOf(
                KEY_AUTHORIZED to authorized,
                KEY_MESSAGE to messages,
                KEY_CODE to 200
            ),
            KEY_TYPE to TYPE_LOGIN_RESULT_MESSAGE
        )
        return LoginResultMessage(
            uuid = UUID.randomUUID().toString(),
            sender = account,
            receiver = account,
            timestamp = System.currentTimeMillis(),
            ext = extensions
        )
    }

    @JvmStatic
    fun buildLogoutMessage(account: Account, code: Int): LogoutMessage {
        val extensions = mapOf(
            KEY_CONTENT to mapOf(
                KEY_CODE to code
            ),
            KEY_TYPE to TYPE_LOGOUT_MESSAGE
        )
        return LogoutMessage(
            uuid = UUID.randomUUID().toString(),
            sender = account,
            receiver = account,
            timestamp = System.currentTimeMillis(),
            ext = extensions
        )
    }

    @JvmStatic
    fun Message.encodeToJSONObject(): JSONObject {
        return mapOf(
            KEY_UUID to this.uuid,
            KEY_TYPE to this.msgType.value,
            KEY_SESSION_TYPE to this.sessionType.value,
            KEY_SENDER to mapOf(
                KEY_SESSION_ID to this.sender.sessionId,
                KEY_ACCOUNT_ID to this.sender.accountId
            ),
            KEY_RECEIVER to mapOf(
                KEY_SESSION_ID to this.receiver.sessionId,
                KEY_ACCOUNT_ID to this.receiver.accountId
            ),
            KEY_TIMESTAMP to this.timestamp,
            KEY_EXTENSION to this.ext
        ).let { map -> JSONObject(map) }
    }

    @JvmStatic
    fun JSONObject.decodeToBasicMessage(): Message? {
        val uuid = this.optString(KEY_UUID)?.takeIf { it.isNotBlank() } ?: return null
        val type = this.optInt(KEY_TYPE).let { typeInt -> MsgType.fromValue(typeInt) }
        val sessionType = this.optInt(KEY_SESSION_TYPE).let { typeInt -> SessionType.fromValue(typeInt) }
        val timestamp = this.optLong(KEY_TIMESTAMP)
        val sender = Account.parse(this.optJSONObject(KEY_SENDER)) ?: return null
        val receiver = Account.parse(this.optJSONObject(KEY_RECEIVER)) ?: return null
        val ext = this.optJSONObject(KEY_EXTENSION)?.map ?: return null
        return MessageImpl(uuid, type, sessionType, sender, receiver, timestamp, ext)
    }
}