package com.application.channel.message.metadata

import com.application.channel.database.AppMessageDatabase
import com.application.channel.database.meta.LocalRecentContact
import com.application.channel.message.SessionType
import com.application.channel.message.map
import com.application.channel.message.meta.Message
import com.application.channel.message.meta.MessageParser
import com.application.channel.message.util.LocalMessageConverter.toMessage
import org.json.JSONObject

/**
 * @author liuzhongao
 * @since 2024/7/24 23:20
 */
internal object RecentContacts {

    @JvmStatic
    fun LocalRecentContact.toRecentContact(message: Message?, unreadCount: Int): RecentContact {
        return RecentContact(
            sessionId = this.sessionId,
            unreadCount = unreadCount,
            recentMessage = message,
            recentTimestamp = this.recentTimestamp,
            sessionType = SessionType.fromValue(this.sessionTypeCode),
            extensions = this.extensions?.takeIf { it.isNotBlank() }?.let { extensions ->
                kotlin.runCatching { JSONObject(extensions) }
                    .onFailure { it.printStackTrace() }
                    .getOrNull()?.map
            } ?: emptyMap()
        )
    }

    @JvmStatic
    suspend fun LocalRecentContact.toRecentContact(database: AppMessageDatabase, messageParser: MessageParser): RecentContact {
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
        return RecentContact(
            sessionId = this.sessionId,
            unreadCount = unreadCount,
            recentMessage = if (recentMessage != null) {
                messageParser.parse(recentMessage)
            } else null,
            recentTimestamp = this.recentTimestamp,
            sessionType = SessionType.fromValue(this.sessionTypeCode),
            extensions = this.extensions?.takeIf { it.isNotBlank() }?.let { extensions ->
                kotlin.runCatching { JSONObject(extensions) }
                    .onFailure { it.printStackTrace() }
                    .getOrNull()?.map
            } ?: emptyMap()
        )
    }

    @JvmStatic
    fun RecentContact.toLocalRecentContact(): LocalRecentContact {
        return LocalRecentContact(
            sessionId = this.sessionId,
            sessionTypeCode = this.sessionType.value,
            recentTimestamp = this.recentTimestamp,
            extensions = kotlin.runCatching { JSONObject(this.extensions) }
                .onFailure { it.printStackTrace() }.getOrNull()?.toString()
        )
    }
}