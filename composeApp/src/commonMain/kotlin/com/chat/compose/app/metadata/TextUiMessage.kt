package com.chat.compose.app.metadata

import androidx.compose.runtime.Immutable
import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/6/16 20:51
 */
@Immutable
data class TextUiMessage(
    override val uuid: String,
    override val sessionType: SessionType,
    override val timestamp: Long,
    val textContent: String
) : UiMessage