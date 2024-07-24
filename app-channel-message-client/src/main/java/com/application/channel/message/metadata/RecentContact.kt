package com.application.channel.message.metadata

import com.application.channel.message.AccessibleMutableMap
import com.application.channel.message.SessionType
import com.application.channel.message.meta.Message
import java.io.Serializable

/**
 * @author liuzhongao
 * @since 2024/7/24 22:26
 */

sealed interface RecentContact : Serializable {
    val sessionId: String
    val sessionType: SessionType
    val unreadCount: Int
    val recentTimestamp: Long
    val recentMessage: Message?
    val extensions: Map<String, Any?>
}

sealed interface MutableRecentContact : RecentContact {

    override var recentTimestamp: Long
    override val extensions: AccessibleMutableMap<String, Any?>

    val isAccessed: Boolean
    val isModified: Boolean
}

fun RecentContact(
    sessionId: String,
    sessionType: SessionType,
    unreadCount: Int = 0,
    recentTimestamp: Long = System.currentTimeMillis(),
    recentMessage: Message? = null,
    extensions: Map<String, Any?> = emptyMap()
): RecentContact {
    return RecentContactImpl(
        sessionId = sessionId,
        sessionType = sessionType,
        unreadCount = unreadCount,
        recentTimestamp = recentTimestamp,
        recentMessage = recentMessage,
        extensions = extensions,
    )
}

private data class RecentContactImpl(
    override val sessionId: String,
    override val sessionType: SessionType,
    override val unreadCount: Int = 0,
    override val recentTimestamp: Long = System.currentTimeMillis(),
    override val recentMessage: Message? = null,
    override val extensions: Map<String, Any?> = emptyMap()
) : RecentContact {
    companion object {
        private const val serialVersionUID: Long = 8878108518466210888L
    }
}

fun RecentContact.mutableRecentContact(): MutableRecentContact {
    if (this is MutableRecentContact) {
        return this
    }
    return MutableRecentContactImpl(
        sessionId = this.sessionId,
        sessionType = this.sessionType,
        unreadCount = this.unreadCount,
        recentTimestamp = this.recentTimestamp,
        recentMessage = this.recentMessage,
        extensions = this.extensions.toMutableMap(),
    )
}

fun RecentContact.immutableRecentContact(): RecentContact {
    if (this is MutableRecentContact) {
        return RecentContact(
            sessionId = this.sessionId,
            sessionType = this.sessionType,
            unreadCount = this.unreadCount,
            recentTimestamp = this.recentTimestamp,
            recentMessage = this.recentMessage,
            extensions = this.extensions,
        )
    }
    return this
}

private class MutableRecentContactImpl(
    override val sessionId: String,
    override val sessionType: SessionType,
    override val unreadCount: Int,
    recentTimestamp: Long,
    override val recentMessage: Message?,
    extensions: MutableMap<String, Any?>
) : MutableRecentContact {

    override var recentTimestamp: Long = recentTimestamp
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

        other as MutableRecentContactImpl

        if (sessionId != other.sessionId) return false
        if (extensions != other.extensions) return false
        if (sessionType != other.sessionType) return false
        if (unreadCount != other.unreadCount) return false
        if (recentTimestamp != other.recentTimestamp) return false
        if (recentMessage != other.recentMessage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sessionId.hashCode()
        result = 31 * result + extensions.hashCode()
        result = 31 * result + sessionType.hashCode()
        result = 31 * result + unreadCount
        result = 31 * result + recentTimestamp.hashCode()
        result = 31 * result + (recentMessage?.hashCode() ?: 0)
        return result
    }
}