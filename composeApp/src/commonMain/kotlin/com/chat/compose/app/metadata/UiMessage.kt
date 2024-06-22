package com.chat.compose.app.metadata

import androidx.compose.runtime.Stable
import com.application.channel.message.SessionType

/**
 * @author liuzhongao
 * @since 2024/6/16 19:37
 */
@Stable
sealed interface UiMessage {

    val uuid: String

    val sessionType: SessionType

    val timestamp: Long

    val isSenderMessage: Boolean

    val isReceiverMessage: Boolean
}

