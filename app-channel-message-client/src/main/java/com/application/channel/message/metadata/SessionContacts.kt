package com.application.channel.message.metadata

import com.application.channel.database.AppMessageDatabase
import com.application.channel.database.meta.LocalSessionContact
import com.application.channel.message.SessionType
import com.application.channel.message.map
import com.application.channel.message.meta.Message
import com.application.channel.message.meta.MessageParser
import com.application.channel.message.util.LocalMessageConverter.toMessage
import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/6/10 14:06
 */
internal object SessionContacts {

    @JvmStatic
    fun LocalSessionContact.toSessionContact(message: Message?, unreadCount: Int): SessionContact {
        return SessionContact(
            sessionId = this.sessionId,
            unreadCount = unreadCount,
            recentMessage = message,
            lastUpdateTimestamp = this.lastModifyTimestamp,
            sessionType = SessionType.fromValue(this.sessionTypeCode),
            extensions = this.extensions?.takeIf { it.isNotBlank() }?.let { extensions ->
                kotlin.runCatching { JSONObject(extensions) }
                    .onFailure { it.printStackTrace() }
                    .getOrNull()?.map
            } ?: emptyMap()
        )
    }

    @JvmStatic
    suspend fun LocalSessionContact.toSessionContact(database: AppMessageDatabase, messageParser: MessageParser): SessionContact {
        val recentMessage = database.messageDao.queryRecentMessageWithSomeone(
            sessionId = database.userSessionId,
            chatterSessionId = this.sessionId,
            sessionTypeCode = this.sessionTypeCode
        )?.toMessage()
        val unreadCount = database.messageDao.calculateUserSessionUnreadCount(
            sessionId = database.userSessionId,
            chatterSessionId = this.sessionId,
            sessionTypeCode = this.sessionTypeCode
        )
        return SessionContact(
            sessionId = this.sessionId,
            unreadCount = unreadCount,
            recentMessage = if (recentMessage != null) {
                messageParser.parse(recentMessage)
            } else null,
            lastUpdateTimestamp = this.lastModifyTimestamp,
            sessionType = SessionType.fromValue(this.sessionTypeCode),
            extensions = this.extensions?.takeIf { it.isNotBlank() }?.let { extensions ->
                kotlin.runCatching { JSONObject(extensions) }
                    .onFailure { it.printStackTrace() }
                    .getOrNull()?.map
            } ?: emptyMap()
        )
    }

    @JvmStatic
    fun SessionContact.toLocalSessionContact(): LocalSessionContact {
        return LocalSessionContact(
            sessionId = this.sessionId,
            sessionTypeCode = this.sessionType.value,
            lastModifyTimestamp = this.lastUpdateTimestamp,
            extensions = kotlin.runCatching { JSONObject(this.extensions) }
                .onFailure { it.printStackTrace() }.getOrNull()?.toString()
        )
    }
}