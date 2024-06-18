package com.chat.compose.app.metadata

import androidx.compose.runtime.Immutable
import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/6/18 01:26
 */
@Immutable
data class UiSessionContact(
    val sessionId: String,
    val sessionType: SessionType,
    val sessionContactName: String,
    val unreadCount: Int,
    val displayContent: String,
    val time: String
)