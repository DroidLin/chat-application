package com.chat.compose.app.metadata

import androidx.compose.runtime.Immutable
import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/6/18 01:26
 */
@Immutable
data class UiRecentContact(
    val sessionId: String,
    val sessionType: SessionType,
    val sessionContactName: String,
    val sessionContactUserId: Long,
    val unreadCount: Int,
    val draftMessage: String?,
    val showingContent: String,
    val timestamp: Long,
    val time: String,
)