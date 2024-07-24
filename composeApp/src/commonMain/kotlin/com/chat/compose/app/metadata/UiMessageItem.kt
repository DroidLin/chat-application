package com.chat.compose.app.metadata

import androidx.compose.runtime.Immutable

/**
 * @author liuzhongao
 * @since 2024/6/22 11:49
 */
@Immutable
data class UiMessageItem(
    val uiRecentContact: UiRecentContact?,
    val uiMessage: UiMessage,
)
