package com.chat.compose.app.metadata

import com.application.channel.im.draftMessage
import com.application.channel.message.metadata.RecentContact
import com.application.channel.message.metadata.SessionContact
import com.chat.compose.app.util.formatTime
import com.chat.compose.app.util.profile

/**
 * @author liuzhongao
 * @since 2024/6/18 21:50
 */
fun RecentContact.toUiRecentContact(): UiRecentContact {
    val profile = this.profile
    return UiRecentContact(
        sessionId = this.sessionId,
        sessionType = this.sessionType,
        sessionContactName = profile?.userInfo?.userName ?: "${this.sessionId} [Test]",
        sessionContactUserId = profile?.userInfo?.userId ?: -1,
        unreadCount = this.unreadCount,
        draftMessage = this.draftMessage,
        showingContent = this.recentMessage?.showingContent ?: "",
        timestamp = this.recentMessage?.timestamp ?: this.recentTimestamp,
        time = formatTime(this.recentMessage?.timestamp ?: this.recentTimestamp)
    )
}


fun SessionContact.toUiSessionContact(): UiSessionContact {
    val profile = this.profile
    return UiSessionContact(
        sessionId = this.sessionId,
        sessionType = this.sessionType,
        userName = profile?.userInfo?.userName ?: "",
        userId = profile?.userInfo?.userId ?: -1,
    )
}