package com.chat.compose.app.metadata

import com.application.channel.im.draftMessage
import com.application.channel.im.userId
import com.application.channel.im.userName
import com.application.channel.message.metadata.SessionContact
import com.chat.compose.app.util.formatTime

/**
 * @author liuzhongao
 * @since 2024/6/18 21:50
 */
fun SessionContact.toUiSessionContact(): UiSessionContact {
    val draftMessage = this.draftMessage?.takeIf { it.isNotBlank() }
    return UiSessionContact(
        sessionId = this.sessionId,
        sessionType = this.sessionType,
        sessionContactName = this.userName ?: "${this.sessionId} [Test]",
        sessionContactUserId = this.userId,
        unreadCount = this.unreadCount,
        displayContent = draftMessage ?: this.recentMessage?.showingContent ?: "",
        time = formatTime(this.lastUpdateTimestamp)
    )
}
