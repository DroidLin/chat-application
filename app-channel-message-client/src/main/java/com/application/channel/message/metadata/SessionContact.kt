package com.application.channel.message.metadata

import com.application.channel.message.AccessibleMutableMap
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import java.io.Serializable

/**
 * @author liuzhongao
 * @since 2024/6/9 11:55
 */
sealed interface SessionContact : Serializable {
    val sessionId: String
    val sessionType: SessionType
    val unreadCount: Int
    val lastUpdateTimestamp: Long
    val recentMessage: Message?
    val extensions: Map<String, Any?>
}

sealed interface MutableSessionContact : SessionContact {

    override var lastUpdateTimestamp: Long
    override val extensions: AccessibleMutableMap<String, Any?>

    val isAccessed: Boolean
    val isModified: Boolean
}

fun SessionContact(
    sessionId: String,
    sessionType: SessionType,
    unreadCount: Int = 0,
    lastUpdateTimestamp: Long = System.currentTimeMillis(),
    recentMessage: Message? = null,
    extensions: Map<String, Any?> = emptyMap()
): SessionContact {
    return SessionContactImpl(
        sessionId = sessionId,
        sessionType = sessionType,
        unreadCount = unreadCount,
        lastUpdateTimestamp = lastUpdateTimestamp,
        recentMessage = recentMessage,
        extensions = extensions,
    )
}

private data class SessionContactImpl(
    override val sessionId: String,
    override val sessionType: SessionType,
    override val unreadCount: Int = 0,
    override val lastUpdateTimestamp: Long = System.currentTimeMillis(),
    override val recentMessage: Message? = null,
    override val extensions: Map<String, Any?> = emptyMap()
) : SessionContact {
    companion object {
        private const val serialVersionUID: Long = 8878108518466210888L
    }
}

fun SessionContact.mutableSessionContact(): MutableSessionContact {
    if (this is MutableSessionContact) {
        return this
    }
    return MutableSessionContactImpl(
        sessionId = this.sessionId,
        sessionType = this.sessionType,
        unreadCount = this.unreadCount,
        lastUpdateTimestamp = this.lastUpdateTimestamp,
        recentMessage = this.recentMessage,
        extensions = this.extensions.toMutableMap(),
    )
}

fun SessionContact.immutableSessionContact(): SessionContact {
    if (this is MutableSessionContact) {
        return SessionContact(
            sessionId = this.sessionId,
            sessionType = this.sessionType,
            unreadCount = this.unreadCount,
            lastUpdateTimestamp = this.lastUpdateTimestamp,
            recentMessage = this.recentMessage,
            extensions = this.extensions,
        )
    }
    return this
}

private class MutableSessionContactImpl(
    override val sessionId: String,
    override val sessionType: SessionType,
    override val unreadCount: Int,
    lastUpdateTimestamp: Long,
    override val recentMessage: Message?,
    extensions: MutableMap<String, Any?>
) : MutableSessionContact {

    override var lastUpdateTimestamp: Long = lastUpdateTimestamp
        get() {
            this._isAccessed = true
            return field
        }
        set(value) {
            this._isAccessed = true
            this._modified = true
            field = value
        }

    override val extensions: AccessibleMutableMap<String, Any?> = AccessibleMutableMap(extensions)

    private var _isAccessed: Boolean = false
    private var _modified: Boolean = false

    override val isAccessed: Boolean get() = this._isAccessed || this.extensions.isAccessed
    override val isModified: Boolean = this._modified || this.extensions.isModified

    companion object {
        private const val serialVersionUID: Long = 2797316274780534338L
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MutableSessionContactImpl

        if (sessionId != other.sessionId) return false
        if (extensions != other.extensions) return false
        if (sessionType != other.sessionType) return false
        if (unreadCount != other.unreadCount) return false
        if (lastUpdateTimestamp != other.lastUpdateTimestamp) return false
        if (recentMessage != other.recentMessage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sessionId.hashCode()
        result = 31 * result + extensions.hashCode()
        result = 31 * result + sessionType.hashCode()
        result = 31 * result + unreadCount
        result = 31 * result + lastUpdateTimestamp.hashCode()
        result = 31 * result + (recentMessage?.hashCode() ?: 0)
        return result
    }
}