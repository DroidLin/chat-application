package com.chat.compose.app.metadata

import androidx.compose.runtime.Immutable
import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/7/26 21:13
 */
@Immutable
data class UiSessionContact(
    val sessionId: String,
    val sessionType: SessionType,
    val userName: String,
    val userId: Long,
)
