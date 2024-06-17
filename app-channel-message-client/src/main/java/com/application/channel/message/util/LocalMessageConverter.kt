package com.application.channel.message.util

import com.application.channel.database.meta.LocalMessage
import com.application.channel.message.meta.Message
import com.application.channel.message.meta.Messages.decodeToBasicMessage
import com.application.channel.message.meta.Messages.encodeToJSONObject
import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/6/9 12:09
 */
internal object LocalMessageConverter {

    @JvmStatic
    fun LocalMessage.toMessage(): Message? = this@LocalMessageConverter.convert(this)

    @JvmStatic
    @JvmOverloads
    fun Message.toLocalMessage(id: Int = 0): LocalMessage = this@LocalMessageConverter.convert(this, id)

    @JvmStatic
    fun convert(localMessage: LocalMessage): Message? {
        val messageContent = localMessage.messageContent
        if (messageContent.isBlank() || messageContent.isEmpty()) return null

        val jsonObject = kotlin.runCatching {
            JSONObject(messageContent)
        }.onFailure { it.printStackTrace() }.getOrNull() ?: return null

        return jsonObject.decodeToBasicMessage()
    }

    @JvmStatic
    fun convert(message: Message, id: Int = 0): LocalMessage {
        return LocalMessage(
            id = id,
            uuid = message.uuid,
            sessionTypeCode = message.sessionType.value,
            stateUserConsumed = false,
            senderSessionId = message.sender.sessionId,
            receiverSessionId = message.receiver.sessionId,
            timestamp = message.timestamp,
            messageContent = message.encodeToJSONObject().toString(),
            extensions = JSONObject(message.ext).toString()
        )
    }
}