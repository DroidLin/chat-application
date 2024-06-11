package com.application.channel.message.metadata

import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import java.io.Serializable

/**
 * @author liuzhongao
 * @since 2024/6/9 11:55
 */
data class SessionContact(
    val sessionId: String,
    val sessionType: SessionType,
    val unreadCount: Int = 0,
    val lastUpdateTimestamp: Long = System.currentTimeMillis(),
    val recentMessage: Message? = null,
    val extensions: Map<String, Any?> = emptyMap()
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 8878108518466210888L
    }
}
