package com.chat.compose.app.metadata

import androidx.compose.runtime.Immutable

/**
 * @author liuzhongao
 * @since 2024/6/18 01:26
 */
@Immutable
data class UiSessionContact(
    val sessionId: String,
    val sessionContactName: String,
    val unreadCount: Int,
)